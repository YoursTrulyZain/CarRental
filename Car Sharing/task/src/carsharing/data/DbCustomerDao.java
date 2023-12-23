package carsharing.data;

import carsharing.entities.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbCustomerDao implements CustomerDao {
    private final DatabaseManager databaseManager;
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CUSTOMER(" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(255) UNIQUE NOT NULL," +
            "RENTED_CAR_ID INT ," +
            "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))";
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String INSERT_DATA = "INSERT INTO CUSTOMER (NAME, RENTED_CAR_ID) VALUES (?, ?)";
    private static final String UPDATE_DATA = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

    public DbCustomerDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        createTable();
    }

    private void createTable() {
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(CREATE_DB)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Customers data structure created");
    }

    @Override
    public void add(Customer customer) {
        String tempCustomerName = customer.getCustomerName();

        try(PreparedStatement preparedStatement = databaseManager.prepareStatementWithKeys(INSERT_DATA)) {
            //Set customer name
            preparedStatement.setString(1, tempCustomerName);
            //Set customer rented car ID to NULL
            preparedStatement.setNull(2, Types.INTEGER);
            //Execute insert query and get number of rows effected (if operation was success or not)
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating customer failed, no rows affected.");
            }
            //If insert operation was success return a result set containing the generated id
            try(ResultSet generatedId = preparedStatement.getGeneratedKeys()) {
                if (generatedId.next()) {
                    customer.setID(Optional.of(generatedId.getInt(1)));
                } else {
                    throw new SQLException("Creating customer failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println(tempCustomerName + " added");
    }

    @Override
    public void update(Customer customer) {
        int tempCustomerId = customer.getID().get();
        Optional<Integer> tempRentedCardId = customer.getRentedCarId();

        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(UPDATE_DATA)) {
            if(tempRentedCardId.isPresent()) {
                preparedStatement.setInt(1, tempRentedCardId.get());
            }else {
                preparedStatement.setNull(1, Types.INTEGER);
            }
            preparedStatement.setInt(2, tempCustomerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } ;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customerList = new ArrayList<>();
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_ALL);
        ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int customerId = resultSet.getInt("ID");
                String customerName = resultSet.getString("NAME");
                Optional<Integer> rentedCarId = resultSet.getInt("RENTED_CAR_ID") == 0 ? Optional.empty() : Optional.of(resultSet.getInt("RENTED_CAR_ID"));

                // Create a Customer object and add it to the list
                customerList.add(new Customer(customerId, customerName, rentedCarId));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customerList;
    }
}