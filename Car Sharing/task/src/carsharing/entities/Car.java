package carsharing.entities;

import java.util.Optional;

public class Car {

    private Optional<Integer> ID = Optional.empty();
    private final String carName;
    private final int companyId;
    private boolean rentStatus;

    public Car(String carName, int companyId) {
        this.carName = carName;
        this.companyId = companyId;
        this.rentStatus = false;
    }

    public Car(int ID, String carName, int companyId, boolean rentStatus) {
        this.ID = Optional.of(ID);
        this.companyId = companyId;
        this.carName = carName;
        this.rentStatus = rentStatus;
    }

    public Optional<Integer> getID() {
        return this.ID;
    }

    public void setID(Optional<Integer> ID) {
        this.ID = ID;
    }

    public String getCarName() {
        return this.carName;
    }

    public int getCompanyID() {
        return this.companyId;
    }

    public boolean isRentStatus() {
        return rentStatus;
    }
    public void setRentStatus(boolean rentStatus) {
        this.rentStatus = rentStatus;
    }

    @Override
    public String toString() {
        return this.getCarName();
    }

}
