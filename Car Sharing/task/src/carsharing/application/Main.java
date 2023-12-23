package carsharing.application;

import carsharing.data.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DatabaseManager databaseManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);
        CompanyDao companyDao = new DbCompanyDao(databaseManager);
        CarDao carDao = new DbCarDao(databaseManager);
        CustomerDao customerDao = new DbCustomerDao(databaseManager);
        MainMenu mainMenu = new MainMenu(scanner, carDao, companyDao, customerDao);
        mainMenu.displayMainMenu();
    }
}