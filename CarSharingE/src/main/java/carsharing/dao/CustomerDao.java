package carsharing.dao;

import carsharing.common.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAllCustomers();

    boolean addCustomer(String name);

    void setCar(int customerID, int carID);

    String getCarName(Customer customer);

    String getCompanyName(Customer customer);
}
