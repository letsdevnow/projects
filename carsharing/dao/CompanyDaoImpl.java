package carsharing.dao;

import carsharing.common.Company;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {

    Connection connection;

    public CompanyDaoImpl(Connection connection) {
        this.connection = connection;
    }

    //return list of all companies
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();

        try {
            Statement st = connection.createStatement();
            st.execute("SELECT * FROM company ORDER BY name");
            ResultSet resultSet = st.getResultSet();
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("ID"), resultSet.getString("name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return companies;
    }

    //add new company
    public boolean addCompany(String name) {
        try {
            Statement st = connection.createStatement();
            st.executeUpdate("INSERT INTO company (name) VALUES ('%s')".formatted(name));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //get company by ID
    public Company getCompany(int id) {
        try {
            Statement st = connection.createStatement();
            st.execute("SELECT name FROM company WHERE ID=%d".formatted(id));
            ResultSet resultSet = st.getResultSet();
            if (resultSet.next()) {
                return new Company(id, resultSet.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
