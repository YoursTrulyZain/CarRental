package carsharing.entities;

import java.util.Optional;

public class Customer {

    private Optional<Integer> ID;
    private String customerName;
    private Optional<Integer> rentedCarId;

    public Customer(String customerName) {
        this.ID = Optional.empty();
        this.customerName = customerName;
        this.rentedCarId = Optional.empty();
    }

    public Customer(int ID, String customerName, Optional<Integer> rentedCarId) {
        this.ID = Optional.of(ID);
        this.customerName = customerName;
        this.rentedCarId = rentedCarId;
    }

    public Optional<Integer> getID() {
        return this.ID;
    }

    public void setID(Optional<Integer> ID) {
        this.ID = ID;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Optional<Integer> getRentedCarId() {
        return this.rentedCarId;
    }

    public void setRentedCarId(Optional<Integer> rentedCarId) {
        this.rentedCarId = rentedCarId;
    }

    @Override
    public String toString() {
        return this.getID().get() + ". " + this.getCustomerName();
    }
}
