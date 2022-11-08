package client;
import lombok.Getter;

public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main (String[] args) {
        System.out.println("Client started");
        Menu menu = new Menu(ADDRESS, PORT);
        menu.showMainMenu();
    }

    public static void println(String str) {
        System.out.println(str);
    }
    public static void println(int i) {
        System.out.println(i);
    }
}
