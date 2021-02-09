package myToDo;

import myToDo.manager.ToDoManager;
import myToDo.manager.UserManager;
import myToDo.models.ToDo;
import myToDo.models.ToDoStatus;
import myToDo.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyToDo implements Commands {
    private static Scanner scanner = new Scanner(System.in);
    private static UserManager userManager = new UserManager();
    private static ToDoManager toDoManager = new ToDoManager();
    private static User currentUser = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printMainCommands();
            String commands = scanner.nextLine();
            switch (commands) {
                case EXIT:
                    System.exit(0);
                    break;
                case LOGIN:
                    login();
                    break;
                case REGISTER:
                    register();
                    break;
                default:
                    System.out.println("WRONG COMMAND!");
            }
        }
    }

    private static void login() {
        System.out.println("PLease input your email and password for login");
        try {
            String data = scanner.nextLine();
            String[] dataArr = data.split(",");
            User user = userManager.getByEmailAndPassword(dataArr[0], dataArr[1]);
            if (user != null) {
                currentUser = user;
                loginSuccess();
            } else
                System.out.println("Wrong email or password");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Wrong data");
        }
    }

    private static void loginSuccess() {
        System.out.println("Welcome " + currentUser.getName());
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            String commands = scanner.nextLine();
            switch (commands) {
                case LOGOUT:
                    isRun = false;
                    currentUser = null;
                    break;
                case ADD_NEW_TODO:
                    addToDo();
                    break;
                case MY_ALL_LIST:
                    printToDos(toDoManager.getAllToDosByUser(currentUser.getId()));
                    break;
                case MY_TODO_LIST:
                    printToDos(toDoManager.getAllToDosByUserIdAndStatus(currentUser.getId(), ToDoStatus.TODO));
                    break;
                case MY_IN_PROGRESS_LIST:
                    printToDos(toDoManager.getAllToDosByUserIdAndStatus(currentUser.getId(), ToDoStatus.IN_PROGRESS));
                    break;
                case MY_FINISHED_LIST:
                    printToDos(toDoManager.getAllToDosByUserIdAndStatus(currentUser.getId(), ToDoStatus.FINISHED));
                    break;
                case CHANGE_TODO_STATUS:
                    changeToDoStatus();
                    break;
                case DELETE_TODO:
                    deleteToDo();
                    break;
                default:
                    System.out.println("WRONG COMMAND!");
            }
        }
    }

    private static void printToDos(List<ToDo> allTodos) {
        for (ToDo allTodo : allTodos) {
            System.out.println(allTodo);
        }
    }

    private static void changeToDoStatus() {
        System.out.println("Please choose todo from list");
        List<ToDo> allToDosByUser = toDoManager.getAllToDosByUser(currentUser.getId());
        for (ToDo toDo : allToDosByUser) {
            System.out.println(toDo);
        }
        long id = Long.parseLong(scanner.nextLine());
        ToDo byId = toDoManager.getById(id);
        if (byId.getUser().getId() == currentUser.getId()) {
            System.out.println("Please choose Status");
            System.out.println(Arrays.toString(ToDoStatus.values()));
            ToDoStatus status = ToDoStatus.valueOf(scanner.nextLine());
            if (toDoManager.update(id, status)) {
                System.out.println("Status was changed");
            } else {
                System.out.println("Something went wrong");
            }
        } else
            System.out.println("Wrong ID");
    }

    private static void deleteToDo() {
        System.out.println("Select todo from list");
        List<ToDo> allToDosByUser = toDoManager.getAllToDosByUser(currentUser.getId());
        for (ToDo toDo : allToDosByUser) {
            System.out.println(toDo);
        }
        long id = Long.parseLong(scanner.nextLine());
        ToDo byId = toDoManager.getById(id);
        if (byId.getUser().getId() == currentUser.getId()) {
            toDoManager.delete(id);
        } else
            System.out.println("Wrong ID");
    }

    private static void addToDo() {
        System.out.println("Please input title, deadline (This format: yyyy-MM-dd HH:mm:ss )");
        String todoData = scanner.nextLine();
        String[] todoDataArr = todoData.split(",");
        ToDo toDo = new ToDo();
        try {
            String title = todoDataArr[0];
            toDo.setTitle(title);
            try {
                if (todoDataArr[1] != null) {
                    toDo.setDeadline(sdf.parse(todoDataArr[1]));
                }
            } catch (IndexOutOfBoundsException e) {
            } catch (ParseException e) {
                System.out.println("Please input date by this format: yyyy-MM-dd HH:mm:ss");
            }
            toDo.setStatus(ToDoStatus.TODO);
            toDo.setUser(currentUser);
            if (toDoManager.createToDo(toDo)) {
                System.out.println("ToDo was added");
            } else
                System.out.println("Something went wrong");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Wrong data!");
        }
    }

    private static void register() {
        System.out.println("Please input name,surname,email,password");
        try {
            String userData = scanner.nextLine();
            String[] userDataArr = userData.split(",");
            User byEmail = userManager.getByEmail(userDataArr[2]);
            if (byEmail == null) {
                User user = User.builder()
                        .name(userDataArr[0])
                        .surname(userDataArr[1])
                        .email(userDataArr[2])
                        .password(userDataArr[3])
                        .build();
                if (userManager.register(user)) {
                    System.out.println(user.getName() + "was successfully added");
                } else System.out.println("Something went wrong");
            } else System.out.println("User already exists!");
        } catch (
                IndexOutOfBoundsException e) {
            System.out.println("Wrong data");
        }
    }
}
