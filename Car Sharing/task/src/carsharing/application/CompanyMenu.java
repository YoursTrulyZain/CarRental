package carsharing.application;

import carsharing.data.*;
import carsharing.entities.Company;
import java.util.List;
import java.util.Scanner;

public class CompanyMenu {

    private final Scanner scanner;
    private final CompanyDao companyDao;
    private final CarMenu carMenu;
    public CompanyMenu(Scanner scanner, CarDao carDao, CompanyDao companyDao) {
        this.scanner = scanner;
        this.companyDao = companyDao;
        this.carMenu = new CarMenu(scanner, carDao);
    }

    public void createCompany() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        Company newCompany = new Company(companyName);
        companyDao.add(newCompany);
    }

    public void companyList() {
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
                companyActionMenu(tempCompany);
            }
        }
    }

    public void companyActionMenu(Company company) {
        int companyActionMenuChoice;
        String companyName = company.getName();
        int companyId = company.getID().get();
        do {
            System.out.println(companyName + " company");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            companyActionMenuChoice = scanner.nextInt();
            scanner.nextLine();
            switch (companyActionMenuChoice) {
                case 1:
                    carMenu.carList(companyId);
                    break;
                case 2:
                    carMenu.createCar(companyId);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (companyActionMenuChoice != 0);
    }
}
