package carsharing.common;

import carsharing.dao.CarDaoImpl;
import carsharing.dao.CompanyDaoImpl;
import carsharing.dao.CustomerDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static carsharing.Main.println;

public class CarSharing {

    private final Scanner sc = new Scanner(System.in);
    private Connection connection;
    private Statement st;
    private CompanyDaoImpl companyDao;
    private CarDaoImpl carDao;
    private CustomerDaoImpl customerDao;

    public CarSharing(String connectionString) {
        try {
            connection = DriverManager.getConnection(connectionString);
            connection.setAutoCommit(true);
            companyDao = new CompanyDaoImpl(connection);
            carDao = new CarDaoImpl(connection);
            customerDao = new CustomerDaoImpl(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //creating of database tables and filling in with test data
    private void createDB() {
        try {
            st = connection.createStatement();

            st.executeUpdate("CREATE TABLE company " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR UNIQUE NOT NULL);");

            st.executeUpdate("CREATE TABLE car " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR UNIQUE NOT NULL, " +
                    "company_ID INT NOT NULL, " +
                    "FOREIGN KEY (company_ID) " +
                    "REFERENCES company(ID)" +
                    ");");

            st.executeUpdate("CREATE TABLE customer " +
                    "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR UNIQUE NOT NULL, " +
                    "rented_car_ID INT, " +
                    "FOREIGN KEY (rented_car_ID) " +
                    "REFERENCES car(ID)" +
                    ");");

            st.executeUpdate("INSERT INTO company (name) values ('Yandex Drive')");
            st.executeUpdate("INSERT INTO company (name) values ('You drive')");
            st.executeUpdate("INSERT INTO car (name, company_ID) values ('Mercedes', 1)");
            st.executeUpdate("INSERT INTO car (name, company_ID) values ('BMW', 2)");
            st.executeUpdate("INSERT INTO customer (name) values ('Ivan')");

            println("Database created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //deleting of database tables
    private void clearDB() {
        try {
            st = connection.createStatement();

            st.executeUpdate("DROP TABLE IF EXISTS customer;");
            st.executeUpdate("DROP TABLE IF EXISTS car;");
            st.executeUpdate("DROP TABLE IF EXISTS company;");

            println("Database cleared, tables deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // main menu
    public void runMainMenu() {
        boolean toExit = false;

        while (!toExit) {
            println("");
            println("1. Log in as a manager");
            println("2. Log in as a customer");
            println("3. Create a customer");
            println("4. Create database");
            println("5. Clear database");
            println("0. Exit");
            String command = sc.nextLine().trim();
            switch (command) {
                case "1" -> runManagerMenu();
                case "2" -> customerListAndMenu();
                case "3" -> createCustomer();
                case "4" -> createDB();
                case "5" -> clearDB();
                case "0" -> {
                    toExit = true;
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                default -> println("Please enter an appropriate command");
            }
        }
    }

    //manager menu
    private void runManagerMenu() {
        println("");
        println("1. Company list");
        println("2. Create a company");
        println("0. Back");
        String command = sc.nextLine().trim();
        switch (command) {
            case "1":
                companyListAndMenu();
                break;
            case "2":
                createCompany();
                break;
            case "0":
                runMainMenu();
            default:
                println("Please enter an appropriate command");
        }
    }

    //******************************************************************************************************************
    //COMPANY block

    //printing of companies list
    private void companyListAndMenu() {
        List<Company> companies = companyList();

        if (companies.size() > 0) {
            println("Choose the company: ");
            if (sc.hasNextInt()) {
                int index = Integer.parseInt(sc.nextLine()) - 1;
                if (index < companies.size() && index > -1) {
                    companyMenu(companies.get(index).getId());
                }

            }
        }
        runManagerMenu();
    }

    //menu of particular company
    private void companyMenu(int companyID) {
        println("");
        println("'" + companyDao.getCompany(companyID).getName() + "' company:");
        println("1. Car list");
        println("2. Create a car");
        println("0. Back");
        String command = sc.nextLine().trim();
        switch (command) {
            case "1":
                carMenu(companyID);
                break;
            case "2":
                createCar(companyID);
                break;
            case "0":
                runManagerMenu();
            default:
                println("Please enter an appropriate command");
        }
        companyMenu(companyID);
    }

    //printing list of companies
    private List<Company> companyList() {
        List<Company> companies = companyDao.getAllCompanies();

        if (companies.size() == 0) {
            println("The company list is empty!");
        } else {
            AtomicInteger counter = new AtomicInteger(1);
            println("");
            println("Choose the company:");
            companies.forEach(c -> {
                println(String.format("%d. %s", counter.getAndIncrement(), c.getName()));
            });
            println("0. Back");
        }

        return companies;
    }

    //creating of new company
    private void createCompany() {
        println("Enter the company name:");
        String name = sc.nextLine().trim();
        if (companyDao.addCompany(name)) {
            println("The company was created!");
        }
        runManagerMenu();
    }

    //******************************************************************************************************************
    //CAR block
    private void carMenu(int companyID) {
        List<Car> cars = carList(companyID);
        companyMenu(companyID);
    }

    //printing list of free cars
    private List<Car> carList(int companyID) {
        List<Car> cars = carDao.getAllFreeCarsInCompany(companyID);

        if (cars.size() == 0) {
            println("The car list is empty!");
        } else {
            AtomicInteger counter = new AtomicInteger(1);
            println("");
            println("Car list:");
            cars.forEach(c -> {
                println(String.format("%d. %s", counter.getAndIncrement(), c.getName()));
            });
        }
        return cars;
    }

    //creating of new car
    private void createCar(int companyID) {
        println("Enter the car name:");
        String name = sc.nextLine().trim();
        if (carDao.addCar(name, companyID)) {
            println("The car was added!");
        }
    }

    //******************************************************************************************************************
    //CUSTOMER block

    //printing of customers list
    private void customerListAndMenu() {
        List<Customer> customers = customerList();

        if (customers.size() > 0) {
            println("0. Back");
            println("Choose the customer: ");
            if (sc.hasNextInt()) {
                int index = Integer.parseInt(sc.nextLine()) - 1;
                if (index < customers.size() && index > -1) {
                    customerMenu(customers.get(index));
                }

            }
        }
        runMainMenu();
    }

    //customer menu
    private void customerMenu(Customer customer) {
        println("");
        println("1. Rent a car");
        println("2. Return a rented car");
        println("3. My rented car");
        println("0. Back");
        String command = sc.nextLine().trim();
        switch (command) {
            case "1":
                rentCarMenu(customer);
                break;
            case "2":
                returnCar(customer);
                break;
            case "3":
                myRentCarMenu(customer);
                break;
            case "0":
                runMainMenu();
            default:
                println("Please enter an appropriate command");
        }
        customerMenu(customer);
    }

    //printing list of customers
    private List<Customer> customerList() {
        List<Customer> customers = customerDao.getAllCustomers();

        if (customers.size() == 0) {
            println("The customer list is empty!");
        } else {
            AtomicInteger counter = new AtomicInteger(1);
            println("");
            println("Customer list:");
            customers.forEach(c -> {
                println(String.format("%d. %s", counter.getAndIncrement(), c.getName()));
            });
        }

        return customers;
    }

    //creating of new customer
    private void createCustomer() {
        println("Enter the customer name:");
        String name = sc.nextLine().trim();
        if (customerDao.addCustomer(name)) {
            println("The customer was added!");
        }
    }

    //menu for renting of car
    private void rentCarMenu(Customer customer) {

        if (customerDao.getCarName(customer) != null) {
            println("You've already rented a car!");
            return;
        }

        List<Company> companies = companyList();

        if (companies.size() == 0) {
            return;
        }

        println("Choose a company: ");
        if (sc.hasNextInt()) {
            int index = Integer.parseInt(sc.nextLine()) - 1;
            if (index < companies.size() && index > -1) {
                List<Car> cars = carList(companies.get(index).getId());

                if (cars.size() == 0) {
                    return;
                }

                println("Choose a car: ");
                if (sc.hasNextInt()) {
                    int carIndex = Integer.parseInt(sc.nextLine()) - 1;
                    if (carIndex < cars.size() && carIndex > -1) {
                        customerDao.setCar(customer.getId(), cars.get(carIndex).getId());
                        customerDao.customerUpdate(customer);
                        println(String.format("You rented '%s'", customerDao.getCarName(customer)));
                    }
                }
            }
        }
    }

    //printing of car
    private void returnCar(Customer customer) {
        if (customerDao.getCarName(customer) != null) {
            customerDao.setCar(customer.getId(), 0);
            customerDao.customerUpdate(customer);
            println("You've returned a rented car!");
        } else {
            println("You didn't rent a car!");
        }
    }

    void myRentCarMenu(Customer customer) {
        String carName = customerDao.getCarName(customer);
        if (carName != null) {
            println("Your rented car:");
            println(carName);
            println("Company:");
            println(customerDao.getCompanyName(customer));
        } else {
            println("You didn't rent a car!");
        }

    }
}
