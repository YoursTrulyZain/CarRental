package carsharing.data;

import carsharing.entities.Car;
import carsharing.entities.Company;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbCompanyDao implements CompanyDao {
    private final DatabaseManager databaseManager;
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS COMPANY(" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(255) UNIQUE NOT NULL )";
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String SELECT_ID = "SELECT * FROM COMPANY WHERE ID = ?";
    private static final String INSERT_DATA = "INSERT INTO COMPANY (name) VALUES (?)";

    public DbCompanyDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        createTable();
    }

    private void createTable() {
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(CREATE_DB)) {
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Companies data structure created");
    }

    @Override
    public void add(Company company) {
        String tempCompanyName = company.getName();

        try(PreparedStatement preparedStatement = databaseManager.prepareStatementWithKeys(INSERT_DATA)) {
            preparedStatement.setString(1, tempCompanyName);

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating company failed, no rows affected.");
            }

            try(ResultSet generatedId = preparedStatement.getGeneratedKeys()) {
                if(generatedId.next()) {
                    company.setID(Optional.of(generatedId.getInt(1)));
                }else {
                    throw new SQLException("Creating company failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println(tempCompanyName + " added");
    }

    @Override
    public List<Company> findAll() {
        List<Company> companyList = new ArrayList<>();
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_ALL);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int companyId = resultSet.getInt("ID");
                String companyName = resultSet.getString("NAME");

                // Create a Customer object and add it to the list
                companyList.add(new Company(companyId, companyName));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return companyList;
    }

    public Company findByID(int companyID) {
        Company tempCompany = null;
        try (PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_ID)) {
            preparedStatement.setInt(1, companyID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int companyId = resultSet.getInt("ID");
                    String companyName = resultSet.getString("NAME");
                    tempCompany = new Company(companyId, companyName);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tempCompany;
    }
}
