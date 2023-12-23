package carsharing.application;

import carsharing.data.*;
import carsharing.entities.Car;
import java.util.List;
import java.util.Scanner;

public class CarMenu {
    private final Scanner scanner;
    private final CarDao carDao;
    public CarMenu(Scanner scanner, CarDao carDao) {
        this.scanner = scanner;
        this.carDao = carDao;
    }

    public void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        Car newCar = new Car(carName, companyId);
        carDao.add(newCar);
    }

    public void carList(int companyId) {
        List<Car> carList = carDao.findAll(companyId);

        if(carList.isEmpty()) {
            System.out.println("\nThe car list is empty!\n");
        }else {
            System.out.println();
            for(int i = 0; i < carList.size(); i++) {
                System.out.println(i+1 + ". " + carList.get(i).getCarName());
            }
        }
    }
}
