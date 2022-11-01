package contacts;

public class Main {

    //obtain db file name in case of existence
    public static void main(String[] args) {
        PhoneBook phoneBook = args.length > 0 ? new PhoneBook(args[0]) : new PhoneBook("contacts.db");
        phoneBook.runMainMenu();
    }

    public static void println(String str) {
        System.out.println(str);
    }
}


