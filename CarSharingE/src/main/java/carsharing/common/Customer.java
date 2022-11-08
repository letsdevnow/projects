package carsharing.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private int carID;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        this.carID = 0;
    }

    Customer(int id, String name, int carID) {
        this.id = id;
        this.name = name;
        this.carID = carID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    @Override
    public String toString() {
        return name;
    }
}

