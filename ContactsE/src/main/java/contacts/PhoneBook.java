package contacts;

import contacts.common.Contact;
import contacts.common.Organization;
import contacts.common.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static contacts.Main.println;

public class PhoneBook {
    private List<Contact> contactsDB = new ArrayList<>();
    private final Scanner sc = new Scanner(System.in);
    private final String fileName;

    //set db file and deserialize if exist
    public PhoneBook(String fileName) {
        this.fileName = fileName;
        if (isDBFileExist()) {
            deserialize();
        }
    }

    //run main menu of the program
    public void runMainMenu() {
        boolean toExit = false;

        while (!toExit) {
            println("[menu] Enter action (add, list, search, count, exit): ");
            String command = sc.nextLine().trim();
            switch (command) {
                case "add" -> addContact();
                case "list" -> listOfContacts();
                case "search" -> search();
                case "count" -> System.out.printf("The Phone Book has %d records.\n", countOfContacts());
                case "exit" -> toExit = true;
                default -> println("Please enter an appropriate command (add, list, search, count, exit): ");
            }
            println("");
        }

    }

    //run record menu
    public void runRecordMenu(int index) {
        println("\n[record] Enter action (edit, delete, menu): ");
        String command = sc.nextLine().trim();
        switch (command) {
            case "edit":
                editContact(index);
                break;
            case "delete":
                removeContact(index);
                break;
            case "menu":
                return;
            default:
                println("No such command");
        }
        println("");
        runRecordMenu(index);
    }

    //contact adding dialog
    public void addContact() {
        println("Enter the type (person, organization):");
        String contactType = sc.nextLine().trim();

        Contact contact;
        if (contactType.equals("person")) {
            contact = new Person();
        } else if (contactType.equals("organization")) {
            contact = new Organization();
        } else {
            println("No such contact type");
            return;
        }

        String[] fields = contact.getFields();
        for (int i = 0; i < fields.length; i++) {
            println("Enter the " + fields[i] + ":");
            contact.setField(fields[i], sc.nextLine().trim());
        }

        contactsDB.add(contact);
        if (fileName != null) {
            serialize();
        }
        println("The record added.");
        println("");
    }

    //contact removing dialog
    public void removeContact(int index) {
        if (index >= 0 && index < countOfContacts()) {
            contactsDB.remove(index);
            println("The record removed!");
            if (fileName != null) {
                serialize();
            }
        }
    }

    //contact editing dialog
    public void editContact(int index) {
        String[] fields = contactsDB.get(index).getFields();
        StringBuilder fieldsStr = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            fieldsStr.append(i == fields.length - 1 ? fields[i] : fields[i] + ", ");
        }

        println("Select a field (" + fieldsStr + "):");
        String command = sc.nextLine().trim();
        if (Arrays.asList(fields).contains(command)) {
            println("Enter " + command + ":");
            contactsDB.get(index).setField(command, sc.nextLine().trim());
            contactsDB.get(index).setEdited();
            if (fileName != null) {
                serialize();
            }
            println("Saved");
            println(contactsDB.get(index).toStringFull());
        }

    }

    //return count of contacts
    public int countOfContacts() {
        return contactsDB.size();
    }

    //DEPRECATED. Was used as dialog of printing details on a record.
    public void printInfo() {
        if (countOfContacts() == 0) {
            println("No records in the Phone book");
        } else {
            listOfContacts();
            println("Select a record:");
            if (sc.hasNextInt()) {
                int index = Integer.parseInt(sc.nextLine()) - 1;
                if (index >= 0 && index < countOfContacts()) {
                    println(contactsDB.get(index).toStringFull());
                }
            }
        }
        println("");
    }

    //print list of contacts and provide details on a record if there is a need
    public void listOfContacts() {
        for (int i = 0; i < contactsDB.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, contactsDB.get(i).toString());
        }
        println("\n[list] Enter action ([number], back): ");
        if (sc.hasNextInt()) {
            int index = Integer.parseInt(sc.nextLine()) - 1;
            println(contactsDB.get(index).toStringFull());
            runRecordMenu(index);
        }
    }

    //search dialog, provide details on a record if there is a need
    public void search() {
        println("Enter search query:");
        String query = sc.nextLine();
        Pattern pattern = Pattern.compile(".*" + query + ".*", Pattern.CASE_INSENSITIVE);

        List<Integer> foundIndexes = new ArrayList<>();
        for (int i = 0; i < contactsDB.size(); i++) {
            if (pattern.matcher(contactsDB.get(i).stringForSearch()).matches()) {
                foundIndexes.add(i);
            }
        }
        println("Found " + foundIndexes.size() + " results");
        for (int i = 0; i < foundIndexes.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, contactsDB.get(foundIndexes.get(i)).toString());
        }

        println("[search] Enter action ([number], back, again): ");
        if (sc.hasNextInt()) {
            int index = Integer.parseInt(sc.nextLine()) - 1;
            println(contactsDB.get(foundIndexes.get(index)).toStringFull());
            runRecordMenu(foundIndexes.get(index));
        } else {
            String command = sc.nextLine().trim();
            switch (command) {
                case "back":
                    break;
                case "again":
                    search();
                    break;
                default:
            }
        }

    }

    //save serialized data into file
    private void serialize() {
        try (
                FileOutputStream fos = new FileOutputStream(fileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(contactsDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load data from file and deserialized it
    private void deserialize() {
        try (
                FileInputStream fis = new FileInputStream(fileName);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);
        ) {
            contactsDB = (List<Contact>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //check whether the file exists and return boolean value
    private boolean isDBFileExist() {
        File file = new File(fileName);
        return file.exists() && !file.isDirectory();
    }
}
