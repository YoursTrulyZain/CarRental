package carsharing.data;

import carsharing.entities.Car;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbCarDao implements CarDao {
    private final DatabaseManager databaseManager;
    private static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS CAR(" +
            "ID INT PRIMARY KEY AUTO_INCREMENT," +
            "NAME VARCHAR(255) UNIQUE NOT NULL," +
            "COMPANY_ID INT NOT NULL," +
            "RENTED BOOLEAN," +
            "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))";
    private static final String SELECT_ALL = "SELECT * FROM CAR WHERE COMPANY_ID = ?";
    private static final String SELECT_AVAILABLE = "SELECT * FROM CAR WHERE COMPANY_ID = ? AND RENTED = FALSE";
    private static final String SELECT_ID = "SELECT * FROM CAR WHERE ID = ?";
    private static final String INSERT_DATA = "INSERT INTO CAR (name, company_id, rented) VALUES (?, ?, ?)";
    private static final String UPDATE_DATA = "UPDATE CAR SET RENTED = ? WHERE ID = ?";

    public DbCarDao(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        createTable();
    }

    private void createTable() {
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(CREATE_DB)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Cars data structure created");
    }

    @Override
    public void add(Car car) {
        String tempCarName = car.getCarName();
        int tempCompanyId = car.getCompanyID();

        try(PreparedStatement preparedStatement = databaseManager.prepareStatementWithKeys(INSERT_DATA)) {
            preparedStatement.setString(1, tempCarName);
            preparedStatement.setInt(2, tempCompanyId);
            preparedStatement.setBoolean(3, car.isRentStatus());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("Creating car failed, no rows affected.");
            }
            try(ResultSet generatedId = preparedStatement.getGeneratedKeys()) {
                if(generatedId.next()) {
                    car.setID(Optional.of(generatedId.getInt(1)));
                }else {
                    throw new SQLException("Creating car failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println(tempCarName + " added");
    }

    @Override
    public void update(Car car) {
        int tempCarId = car.getID().get();
        boolean rentStatus = car.isRentStatus();

        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(UPDATE_DATA)) {
            if(rentStatus) {
                preparedStatement.setBoolean(1, true);
            }else {
                preparedStatement.setBoolean(1, false);
            }
            preparedStatement.setInt(2, tempCarId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } ;
    }

    @Override
    public List<Car> findAll(int companyId) {
        List<Car> carList = new ArrayList<>();
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_ALL)) {
            preparedStatement.setInt(1, companyId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String carName = resultSet.getString("NAME");
                    int tempCompanyId = resultSet.getInt("COMPANY_ID");
                    boolean rentStatus = resultSet.getBoolean("RENTED");
                    carList.add(new Car(id, carName, tempCompanyId, rentStatus));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }

    public List<Car> findAvailable(int companyId) {
        List<Car> carList = new ArrayList<>();
        try(PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_AVAILABLE)) {
            preparedStatement.setInt(1, companyId);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String carName = resultSet.getString("NAME");
                    int tempCompanyId = resultSet.getInt("COMPANY_ID");
                    boolean rentStatus = resultSet.getBoolean("RENTED");
                    carList.add(new Car(id, carName, tempCompanyId, rentStatus));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return carList;
    }

    public Car findByID(int carID) {
        Car tempCar = null;
        try (PreparedStatement preparedStatement = databaseManager.prepareStatement(SELECT_ID)) {
            preparedStatement.setInt(1, carID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String carName = resultSet.getString("NAME");
                    int tempCompanyId = resultSet.getInt("COMPANY_ID");
                    boolean rentStatus = resultSet.getBoolean("RENTED");
                    tempCar = new Car(id, carName, tempCompanyId, rentStatus);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tempCar;
    }
}
