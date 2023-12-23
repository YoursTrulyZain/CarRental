package carsharing.entities;

import java.util.Optional;

public class Company {
    private Optional<Integer> ID;
    private final String companyName;

    public Company(String companyName) {
        this.companyName = companyName;
    }
    public Company(int ID, String companyName) {
        this.ID = Optional.of(ID);
        this.companyName = companyName;
    }
    public Optional<Integer> getID() {
        return this.ID;
    }

    public void setID(Optional<Integer> ID) {
        this.ID = ID;
    }

    public String getName() {
        return this.companyName;
    }

    @Override
    public String toString() {
        return this.getID().get() + ". " + this.getName();
    }

}
