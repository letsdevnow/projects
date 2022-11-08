package client;

import client.exception.KeyNotFoundException;

import java.util.Scanner;
import static client.Main.println;

public class Menu {
    private final String address;
    private final int port;
    private final Scanner sc = new Scanner(System.in);
    private final ClientFunc clientFunc;
    boolean toExit = false;

    public Menu(String address, int port) {
        this.address = address;
        this.port = port;
        clientFunc = new ClientFunc(address, port);
    }

    //main menu
    public void showMainMenu() {
        if(clientFunc.connect()) {
            println("Connected to the server");
        }

/*        for (int i = 0; i < 10000; i++) {
            clientFunc.setValueById("k" + i, "v" + i);
        }*/

        while (!toExit && clientFunc.isConnected()) {
            println("Please enter command (get, set, delete, exit, stop server):");
            String command = sc.nextLine().trim();
            switch (command) {
                case "get" -> showGetMenu();
                case "set" -> showSetMenu();
                case "delete" -> showDeleteMenu();
                case "exit" -> exit();
                case "stop server", "stop" -> stopServer();
                default -> println("Please enter appropriate command");
            }
            println("");
        }
    }

    //get dialog
    private void showGetMenu() {
        println("Please enter key:");
        String key = sc.nextLine().trim();
        try {
            println("Value is: " + clientFunc.getValueById(key));
        }
        catch (KeyNotFoundException e) {
            println("The key has not been found in the database");
        }
    }

    //set dialog
    private void showSetMenu() {
        println("Please enter key:");
        String key = sc.nextLine().trim();
        println("Please enter value:");
        String value = sc.nextLine().trim();

        if (clientFunc.setValueById(key, value)) {
            println("Done");
        } else {
            println("Error occurred");
        }
    }

    //delete dialog
    private void showDeleteMenu() {
        println("Please enter key:");
        String key = sc.nextLine().trim();
        if (clientFunc.deleteValueById(key)) {
            println("Done");
        } else {
            println("Error occurred");
        }
    }

    //just disconnect from server
    private void exit() {
        clientFunc.disconnect(false);
        toExit = true;
    }

    //disconnect from server and shutdown the server
    private void stopServer() {
        clientFunc.disconnect(true);
        toExit = true;
    }

}
