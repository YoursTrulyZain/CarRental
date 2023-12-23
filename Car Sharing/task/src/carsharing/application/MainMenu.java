package carsharing.application;

import carsharing.data.CarDao;
import carsharing.data.CompanyDao;
import carsharing.data.CustomerDao;
import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner;
    private final ManagerMenu managerMenu;
    private final CustomerMenu customerMenu;

    public MainMenu(Scanner scanner, CarDao carDao, CompanyDao companyDao, CustomerDao customerDao) {
        this.scanner = scanner;
        this.managerMenu = new ManagerMenu(scanner, carDao, companyDao);
        this.customerMenu = new CustomerMenu(scanner, carDao, companyDao, customerDao);
    }

    public void displayMainMenu() {
        int mainMenuChoice;
        do {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");
            mainMenuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainMenuChoice) {
                case 1:
                    managerMenu.displayManagerMenu();
                    break;
                case 2:
                    customerMenu.customerList();
                    break;
                case 3:
                    customerMenu.createCustomer();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (mainMenuChoice != 0);
    }


}
