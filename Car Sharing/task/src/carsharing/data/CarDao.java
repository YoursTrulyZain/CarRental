package carsharing.data;

import carsharing.entities.Car;
import java.util.List;

public interface CarDao {

    List<Car> findAll(int companyId);
    List<Car> findAvailable(int companyId);
    Car findByID(int carID);
    void add(Car car);
    void update(Car car);
}
