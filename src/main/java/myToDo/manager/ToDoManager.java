package myToDo.manager;

import myToDo.db.DBConnectionProvider;
import myToDo.models.ToDo;
import myToDo.models.ToDoStatus;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ToDoManager {
    private Connection connection = DBConnectionProvider.getProvider().getConnection();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    private UserManager userManager = new UserManager();

    public boolean createToDo(ToDo toDo) {
        String sql = "INSERT INTO todo(title,deadline,status,user_id) VALUES(?,?,?,?)";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pStatement.setString(1, toDo.getTitle());
            pStatement.setString(2, sdf.format(toDo.getDeadline()));
            pStatement.setString(3, toDo.getStatus().name());
            pStatement.setLong(4, toDo.getUser().getId());
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                toDo.setId(rs.getLong(1));
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    private ToDo getToDoFromResultSet(ResultSet resultSet) {
        try {
            return ToDo.builder()
                    .id(resultSet.getLong(1))
                    .title(resultSet.getString(2))
                    .deadline(sdf.parse(resultSet.getString(3)))
                    .status(ToDoStatus.valueOf(resultSet.getString(4)))
                    .user(userManager.getById(resultSet.getLong(5)))
                    .createdDate(sdf.parse(resultSet.getString(6)))
                    .build();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ToDo> getAllToDosByUser(long userId) {
        List<ToDo> toDos = new ArrayList<ToDo>();
        String sql = "SELECT * FROM todo WHERE id = " + userId;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                getToDoFromResultSet(resultSet);
                return toDos;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<ToDo> getAllToDosByUserIdAndStatus(long userId, ToDoStatus status) {
        List<ToDo> toDos = new ArrayList<ToDo>();
        String sql = "SELECT * FROM todo WHERE id = ? AND status =  ? ";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, userId);
            statement.setString(2, status.name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                toDos.add(getToDoFromResultSet(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean update(long id, ToDoStatus status) {
        String sql = "UPDATE todo SET status = '" + status.name() + "' WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean delete(long id) {
        String sql = "DELETE FROM todo WHERE id = " + id;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

}
