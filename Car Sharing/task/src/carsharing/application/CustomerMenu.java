package carsharing.application;

import carsharing.data.*;
import carsharing.entities.Car;
import carsharing.entities.Company;
import carsharing.entities.Customer;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CustomerMenu {
    private final Scanner scanner;
    private final CustomerDao customerDao;
    private final CompanyDao companyDao;
    private final CarDao carDao;
    public CustomerMenu(Scanner scanner, CarDao carDao, CompanyDao companyDao, CustomerDao customerDao) {
        this.scanner = scanner;
        this.customerDao = customerDao;
        this.companyDao = companyDao;
        this.carDao = carDao;
    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        Customer newCustomer = new Customer(customerName);
        customerDao.add(newCustomer);

    }

    public void customerList() {
        int customerChoice = -1;
        List<Customer> customerList = customerDao.findAll();

        if(customerList.isEmpty()) {
            System.out.println("\nThe customer list is empty!\n");
        }else {
            System.out.println();
            System.out.println("Choose a customer:");
            for (Customer customer : customerList) {
                System.out.println(customer.toString());
            }
            System.out.println("0. Back");
            customerChoice = scanner.nextInt();
            scanner.nextLine();

            if(customerChoice !=0) {
                Customer tempCustomer = customerList.get(customerChoice - 1);
                customerActionMenu(tempCustomer);
            }
        }
    }

    public void customerActionMenu(Customer customer) {
        int customerActionMenuChoice;
        do {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");
            customerActionMenuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (customerActionMenuChoice) {
                case 1:
                    // Rent a car
                    selectRentalCompany(customer);
                    break;
                case 2:
                    // Return a rented car
                    returnCar(customer);
                    break;
                case 3:
                    // My rented car
                    rentalCarInfo(customer);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (customerActionMenuChoice != 0);

    }

    public void selectRentalCompany(Customer customer) {
        if(checkCustomerRentalStatus(customer)) {
            System.out.println("You've already rented a car!");
        }else {
            int companyChoice = -1;
            List<Company> companyList = companyDao.findAll();

            if(companyList.isEmpty()) {
                System.out.println("\nThe company list is empty!\n");
            }else {
                System.out.println();
                System.out.println("Choose a company:");
                for (Company company : companyList) {
                    System.out.println(company.toString());
                }
                System.out.println("0. Back");
                companyChoice = scanner.nextInt();
                scanner.nextLine();

                if(companyChoice !=0) {
                    Company tempCompany = companyList.get(companyChoice - 1);
                    selectRentalCar(customer, tempCompany);
                }
            }
        }
    }

    public void selectRentalCar(Customer customer,Company company) {
        int tempCompanyId = company.getID().get();
        String tempCompanyName = company.getName();
        List<Car> carList = carDao.findAvailable(tempCompanyId);
        int carChoice = -1;

        if(carList.isEmpty()) {
            System.out.println("\nNo available cars in the " + tempCompanyName + " company\n");
        }else {
            System.out.println();
            System.out.println("Choose a car:");
            System.out.println();
            for(int i = 0; i < carList.size(); i++) {
                System.out.println(i+1 + ". " + carList.get(i).getCarName());
            }
            System.out.println("0. Back");
            carChoice = scanner.nextInt();
            scanner.nextLine();

            if(carChoice !=0) {
                Car tempCar = carList.get(carChoice - 1);
                customer.setRentedCarId(tempCar.getID());
                tempCar.setRentStatus(true);
                carDao.update(tempCar);
                customerDao.update(customer);
                System.out.println();
                System.out.println("You rented '" + tempCar.getCarName() +"'");
            }
        }

    }

    public void returnCar(Customer customer) {
        if(checkCustomerRentalStatus(customer)) {
            Car tempCar = carDao.findByID(customer.getRentedCarId().get());
            tempCar.setRentStatus(false);
            carDao.update(tempCar);
            customer.setRentedCarId(Optional.empty());
            customerDao.update(customer);
            System.out.println();
            System.out.println("You've returned a rented car!");
        }else {
            System.out.println();
            System.out.println("You didn't rent a car!");
        }
    }

    public void rentalCarInfo(Customer customer) {
        if(checkCustomerRentalStatus(customer)) {
            Car tempCar = carDao.findByID(customer.getRentedCarId().get());
            Company tempCompany = companyDao.findByID(tempCar.getCompanyID());

            System.out.println();
            System.out.println("Your rented car:");
            System.out.println(tempCar.getCarName());
            System.out.println("Company:");
            System.out.println(tempCompany.getName());
        }else {
            System.out.println("You didn't rent a car!");
        }
    }

    public boolean checkCustomerRentalStatus(Customer customer) {
        if(customer.getRentedCarId().isPresent()) {
            return true;
        }else {
            return false;
        }
    }
}
