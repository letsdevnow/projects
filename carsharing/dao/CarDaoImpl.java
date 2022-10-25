package carsharing.dao;

import carsharing.common.Car;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    Connection connection;

    public CarDaoImpl(Connection connection) {
        this.connection = connection;
    }

    //return list of free cars
    public List<Car> getAllFreeCarsInCompany(int companyID) {
        List<Car> cars = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            st.execute("SELECT * FROM car LEFT JOIN customer ON customer.rented_car_ID=car.ID " +
                    "WHERE customer.rented_car_ID IS NULL AND car.company_ID=%d".formatted(companyID));
            ResultSet resultSet = st.getResultSet();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("car.ID"), resultSet.getString("car.name"),
                        resultSet.getInt("car.company_ID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cars;
    }

    //add new car
    public boolean addCar(String name, int companyID) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("INSERT INTO car (name, company_ID) VALUES ('%s', %d)".formatted(name, companyID));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
