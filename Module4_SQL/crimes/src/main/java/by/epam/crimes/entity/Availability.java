package by.epam.crimes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Availability {
    private String date;
    private List<String> forces;

    public Availability() {
        forces = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getForces() {
        return forces;
    }

    public void setForces(List<String> forces) {
        this.forces = forces;
    }

    public boolean add(String s) {
        return forces.add(s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Availability that = (Availability) o;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return forces != null ? forces.equals(that.forces) : that.forces == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (forces != null ? forces.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Availability.class.getSimpleName() + "[", "]")
                .add("date='" + date + "'")
                .add("forces=" + forces)
                .toString();
    }

}