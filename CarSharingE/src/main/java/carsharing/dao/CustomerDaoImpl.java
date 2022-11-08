package carsharing.dao;

import carsharing.common.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    //return list of all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            st.execute("SELECT * FROM customer ORDER BY name");
            ResultSet resultSet = st.getResultSet();
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt("ID"), resultSet.getString("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }

    //add new customer
    public boolean addCustomer(String name) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("INSERT INTO customer (name) VALUES ('%s')".formatted(name));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //set car for customer or make car free
    public void setCar(int customerID, int carID) {
        try {
            Statement st = connection.createStatement();
            if (carID != 0) {
                st.executeUpdate("UPDATE customer SET rented_car_ID=%d WHERE ID=%d".formatted(carID, customerID));
            } else {
                st.executeUpdate("UPDATE customer SET rented_car_ID=NULL WHERE ID=%d".formatted(customerID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //return car name by ID
    public String getCarName(Customer customer) {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT name FROM car WHERE ID=%d".formatted(customer.getCarID()));
            ResultSet resultSet = st.getResultSet();
            if (resultSet.next()) {
                return resultSet.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //update customer object from DB
    public void customerUpdate(Customer customer) {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT * FROM customer WHERE ID=%d".formatted(customer.getId()));
            ResultSet resultSet = st.getResultSet();
            if (resultSet.next()) {
                customer.setName(resultSet.getString("name"));
                customer.setCarID(resultSet.getInt("rented_car_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //return company name for customer object
    public String getCompanyName(Customer customer) {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT company.name FROM company LEFT JOIN car ON company.ID=car.company_ID WHERE car.ID=%d"
                    .formatted(customer.getCarID()));
            ResultSet resultSet = st.getResultSet();
            if (resultSet.next()) {
                return resultSet.getString("company.name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
