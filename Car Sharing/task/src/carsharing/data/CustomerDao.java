package carsharing.data;

import carsharing.entities.Customer;
import java.util.List;

public interface CustomerDao {
    List<Customer> findAll();
    void add(Customer customer);
    void update(Customer customer);
}