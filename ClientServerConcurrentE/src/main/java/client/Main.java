package client;
import lombok.Getter;

public class Main {
    private final static String REQUEST_FILE_PATH = "\\src\\resources\\data\\client\\";

    public static void main (String[] args) {
        System.out.println("Client started");
        Menu menu = new Menu();
        menu.showMainMenu();

    }

    public static void println(String str) {
        System.out.println(str);
    }
    public static void println(int i) {
        System.out.println(i);
    }
}
