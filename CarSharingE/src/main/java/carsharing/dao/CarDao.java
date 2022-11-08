package carsharing.dao;

import carsharing.common.Car;

import java.util.List;

public interface CarDao {
    List<Car> getAllFreeCarsInCompany(int companyID);

    boolean addCar(String name, int companyID);
}
