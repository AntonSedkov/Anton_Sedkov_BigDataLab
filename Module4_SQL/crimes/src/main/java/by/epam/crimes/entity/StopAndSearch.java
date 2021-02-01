package by.epam.crimes.entity;

import java.util.StringJoiner;

public class StopAndSearch {
    private String force;
    private int idStopAndSearch;
    private String type;
    private boolean involvedPerson;
    private String datetime;
    private String operation;
    private String operationName;
    private Location location;
    private String gender;
    private String ageRange;
    private String selfDefinedEthnicity;
    private String officerDefinedEthnicity;
    private String legislation;
    private String objectOfSearch;
    private String outcome;
    private String outcomeLinkedToObjectOfSearch;
    private Boolean removalOfMoreThanOuterClothing;
    private OutcomeObject outcomeObject;

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public int getIdStopAndSearch() {
        return idStopAndSearch;
    }

    public void setIdStopAndSearch(int idStopAndSearch) {
        this.idStopAndSearch = idStopAndSearch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isInvolvedPerson() {
        return involvedPerson;
    }

    public void setInvolvedPerson(boolean involvedPerson) {
        this.involvedPerson = involvedPerson;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getSelfDefinedEthnicity() {
        return selfDefinedEthnicity;
    }

    public void setSelfDefinedEthnicity(String selfDefinedEthnicity) {
        this.selfDefinedEthnicity = selfDefinedEthnicity;
    }

    public String getOfficerDefinedEthnicity() {
        return officerDefinedEthnicity;
    }

    public void setOfficerDefinedEthnicity(String officerDefinedEthnicity) {
        this.officerDefinedEthnicity = officerDefinedEthnicity;
    }

    public String getLegislation() {
        return legislation;
    }

    public void setLegislation(String legislation) {
        this.legislation = legislation;
    }

    public String getObjectOfSearch() {
        return objectOfSearch;
    }

    public void setObjectOfSearch(String objectOfSearch) {
        this.objectOfSearch = objectOfSearch;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcomeLinkedToObjectOfSearch() {
        return outcomeLinkedToObjectOfSearch;
    }

    public void setOutcomeLinkedToObjectOfSearch(String outcomeLinkedToObjectOfSearch) {
        this.outcomeLinkedToObjectOfSearch = outcomeLinkedToObjectOfSearch;
    }

    public Boolean getRemovalOfMoreThanOuterClothing() {
        return removalOfMoreThanOuterClothing;
    }

    public void setRemovalOfMoreThanOuterClothing(Boolean removalOfMoreThanOuterClothing) {
        this.removalOfMoreThanOuterClothing = removalOfMoreThanOuterClothing;
    }

    public OutcomeObject getOutcomeObject() {
        return outcomeObject;
    }

    public void setOutcomeObject(OutcomeObject outcomeObject) {
        this.outcomeObject = outcomeObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopAndSearch search = (StopAndSearch) o;
        if (idStopAndSearch != search.idStopAndSearch) return false;
        if (involvedPerson != search.involvedPerson) return false;
        if (force != null ? !force.equals(search.force) : search.force != null) return false;
        if (type != null ? !type.equals(search.type) : search.type != null) return false;
        if (datetime != null ? !datetime.equals(search.datetime) : search.datetime != null) return false;
        if (operation != null ? !operation.equals(search.operation) : search.operation != null) return false;
        if (operationName != null ? !operationName.equals(search.operationName) : search.operationName != null)
            return false;
        if (location != null ? !location.equals(search.location) : search.location != null) return false;
        if (gender != null ? !gender.equals(search.gender) : search.gender != null) return false;
        if (ageRange != null ? !ageRange.equals(search.ageRange) : search.ageRange != null) return false;
        if (selfDefinedEthnicity != null ? !selfDefinedEthnicity.equals(search.selfDefinedEthnicity) : search.selfDefinedEthnicity != null)
            return false;
        if (officerDefinedEthnicity != null ? !officerDefinedEthnicity.equals(search.officerDefinedEthnicity) : search.officerDefinedEthnicity != null)
            return false;
        if (legislation != null ? !legislation.equals(search.legislation) : search.legislation != null) return false;
        if (objectOfSearch != null ? !objectOfSearch.equals(search.objectOfSearch) : search.objectOfSearch != null)
            return false;
        if (outcome != null ? !outcome.equals(search.outcome) : search.outcome != null) return false;
        if (outcomeLinkedToObjectOfSearch != null ? !outcomeLinkedToObjectOfSearch.equals(search.outcomeLinkedToObjectOfSearch) : search.outcomeLinkedToObjectOfSearch != null)
            return false;
        if (removalOfMoreThanOuterClothing != null ? !removalOfMoreThanOuterClothing.equals(search.removalOfMoreThanOuterClothing) : search.removalOfMoreThanOuterClothing != null)
            return false;
        return outcomeObject != null ? outcomeObject.equals(search.outcomeObject) : search.outcomeObject == null;
    }

    @Override
    public int hashCode() {
        int result = force != null ? force.hashCode() : 0;
        result = 31 * result + idStopAndSearch;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (involvedPerson ? 1 : 0);
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        result = 31 * result + (operation != null ? operation.hashCode() : 0);
        result = 31 * result + (operationName != null ? operationName.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (ageRange != null ? ageRange.hashCode() : 0);
        result = 31 * result + (selfDefinedEthnicity != null ? selfDefinedEthnicity.hashCode() : 0);
        result = 31 * result + (officerDefinedEthnicity != null ? officerDefinedEthnicity.hashCode() : 0);
        result = 31 * result + (legislation != null ? legislation.hashCode() : 0);
        result = 31 * result + (objectOfSearch != null ? objectOfSearch.hashCode() : 0);
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        result = 31 * result + (outcomeLinkedToObjectOfSearch != null ? outcomeLinkedToObjectOfSearch.hashCode() : 0);
        result = 31 * result + (removalOfMoreThanOuterClothing != null ? removalOfMoreThanOuterClothing.hashCode() : 0);
        result = 31 * result + (outcomeObject != null ? outcomeObject.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StopAndSearch.class.getSimpleName() + "[", "]")
                .add("force='" + force + "'")
                .add("idStopAndSearch=" + idStopAndSearch)
                .add("type='" + type + "'")
                .add("involvedPerson=" + involvedPerson)
                .add("datetime='" + datetime + "'")
                .add("operation='" + operation + "'")
                .add("operationName='" + operationName + "'")
                .add("location=" + location)
                .add("gender='" + gender + "'")
                .add("ageRange='" + ageRange + "'")
                .add("selfDefinedEthnicity='" + selfDefinedEthnicity + "'")
                .add("officerDefinedEthnicity='" + officerDefinedEthnicity + "'")
                .add("legislation='" + legislation + "'")
                .add("objectOfSearch='" + objectOfSearch + "'")
                .add("outcome='" + outcome + "'")
                .add("outcomeLinkedToObjectOfSearch='" + outcomeLinkedToObjectOfSearch + "'")
                .add("removalOfMoreThanOuterClothing=" + removalOfMoreThanOuterClothing)
                .add("outcomeObject=" + outcomeObject)
                .toString();
    }

}