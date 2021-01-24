package by.epam.crimes.entity;

import java.util.StringJoiner;

public class OutcomeStatus {
    private String categoryOutcome;
    private String dateOutcome;

    public String getCategoryOutcome() {
        return categoryOutcome;
    }

    public void setCategoryOutcome(String categoryOutcome) {
        this.categoryOutcome = categoryOutcome;
    }

    public String getDateOutcome() {
        return dateOutcome;
    }

    public void setDateOutcome(String dateOutcome) {
        this.dateOutcome = dateOutcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutcomeStatus that = (OutcomeStatus) o;
        if (categoryOutcome != null ? !categoryOutcome.equals(that.categoryOutcome) : that.categoryOutcome != null)
            return false;
        return dateOutcome != null ? dateOutcome.equals(that.dateOutcome) : that.dateOutcome == null;
    }

    @Override
    public int hashCode() {
        int result = categoryOutcome != null ? categoryOutcome.hashCode() : 0;
        result = 31 * result + (dateOutcome != null ? dateOutcome.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OutcomeStatus.class.getSimpleName() + "[", "]")
                .add("categoryOutcome='" + categoryOutcome + "'")
                .add("dateOutcome='" + dateOutcome + "'")
                .toString();
    }

}