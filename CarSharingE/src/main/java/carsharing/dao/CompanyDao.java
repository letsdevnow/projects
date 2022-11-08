package carsharing.dao;

import carsharing.common.Company;

import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies();

    boolean addCompany(String name);

    Company getCompany(int rollNo);
}
