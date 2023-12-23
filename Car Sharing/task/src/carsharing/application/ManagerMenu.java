package carsharing.application;

import carsharing.data.CarDao;
import carsharing.data.CompanyDao;
import java.util.Scanner;

public class ManagerMenu {

    private final Scanner scanner;
    private final CompanyMenu companyMenu;

    public ManagerMenu(Scanner scanner, CarDao carDao, CompanyDao companyDao) {
        this.scanner = scanner;
        this.companyMenu = new CompanyMenu(scanner, carDao, companyDao);
    }

    public void displayManagerMenu() {
        int managerMenuChoice;
        do {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            managerMenuChoice = scanner.nextInt();
            scanner.nextLine();

            switch (managerMenuChoice) {
                case 1:
                    // Company List
                    companyMenu.companyList();
                    break;
                case 2:
                    // Create a company
                    companyMenu.createCompany();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (managerMenuChoice != 0);
    }
}
