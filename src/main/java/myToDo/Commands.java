package myToDo;

public interface Commands {
    //main commands
    String EXIT = "0";
    String LOGIN = "1";
    String REGISTER = "2";

    //user commands
    String LOGOUT = "0";
    String ADD_NEW_TODO = "1";
    String MY_ALL_LIST = "2";
    String MY_TODO_LIST = "3";
    String MY_IN_PROGRESS_LIST = "4";
    String MY_FINISHED_LIST = "5";
    String CHANGE_TODO_STATUS = "6";
    String DELETE_TODO = "7";

    static void printMainCommands() {
        System.out.println("please input " + EXIT + " for exit");
        System.out.println("please input " + LOGIN + " for login");
        System.out.println("please input " + REGISTER + " for Register");
    }

    static void printUserCommands() {
        System.out.println("please input " + LOGOUT + " for logout");
        System.out.println("please input " + ADD_NEW_TODO + " for ADD_NEW_TODO");
        System.out.println("please input " + MY_ALL_LIST + " for MY_ALL_LIST");
        System.out.println("please input " + MY_TODO_LIST + " for MY_TODO_LIST");
        System.out.println("please input " + MY_IN_PROGRESS_LIST + " for MY_IN_PROGRESS_LIST");
        System.out.println("please input " + MY_FINISHED_LIST + " for MY_FINISHED_LIST");
        System.out.println("please input " + CHANGE_TODO_STATUS + " for CHANGE_TODO_STATUS");
        System.out.println("please input " + DELETE_TODO + " for DELETE_TODO");

    }
}
