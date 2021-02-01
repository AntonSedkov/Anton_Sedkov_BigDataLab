package by.epam.crimes.entity;

import java.util.StringJoiner;

public class OutcomeObject {
    private String idOutcomeObject;
    private String nameOutcomeObject;

    public String getIdOutcomeObject() {
        return idOutcomeObject;
    }

    public void setIdOutcomeObject(String idOutcomeObject) {
        this.idOutcomeObject = idOutcomeObject;
    }

    public String getNameOutcomeObject() {
        return nameOutcomeObject;
    }

    public void setNameOutcomeObject(String nameOutcomeObject) {
        this.nameOutcomeObject = nameOutcomeObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OutcomeObject that = (OutcomeObject) o;
        if (idOutcomeObject != null ? !idOutcomeObject.equals(that.idOutcomeObject) : that.idOutcomeObject != null)
            return false;
        return nameOutcomeObject != null ? nameOutcomeObject.equals(that.nameOutcomeObject) : that.nameOutcomeObject == null;
    }

    @Override
    public int hashCode() {
        int result = idOutcomeObject != null ? idOutcomeObject.hashCode() : 0;
        result = 31 * result + (nameOutcomeObject != null ? nameOutcomeObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OutcomeObject.class.getSimpleName() + "[", "]")
                .add("idOutcomeObject='" + idOutcomeObject + "'")
                .add("nameOutcomeObject='" + nameOutcomeObject + "'")
                .toString();
    }

}