package by.epam.crimes.entity;

import java.util.StringJoiner;

public class Street {
    private int idStreet;
    private String nameStreet;

    public int getIdStreet() {
        return idStreet;
    }

    public void setIdStreet(int idStreet) {
        this.idStreet = idStreet;
    }

    public String getNameStreet() {
        return nameStreet;
    }

    public void setNameStreet(String nameStreet) {
        this.nameStreet = nameStreet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        if (idStreet != street.idStreet) return false;
        return nameStreet != null ? nameStreet.equals(street.nameStreet) : street.nameStreet == null;
    }

    @Override
    public int hashCode() {
        int result = idStreet;
        result = 31 * result + (nameStreet != null ? nameStreet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Street.class.getSimpleName() + "[", "]")
                .add("idStreet=" + idStreet)
                .add("nameStreet='" + nameStreet + "'")
                .toString();
    }

}