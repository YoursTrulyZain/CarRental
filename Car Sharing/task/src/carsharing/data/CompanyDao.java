package carsharing.data;

import carsharing.entities.Car;
import carsharing.entities.Company;
import java.util.List;

public interface CompanyDao {
    List<Company> findAll();
    Company findByID(int companyID);
    void add(Company company);

}
