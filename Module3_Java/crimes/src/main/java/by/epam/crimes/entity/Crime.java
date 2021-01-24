package by.epam.crimes.entity;

import java.util.StringJoiner;

public class Crime {
    private int idCrime;
    private String category;
    private String persistentId;
    private String month;
    private Location location;
    private String context;
    private LocationType locationType;
    private String locationSubtype;
    private OutcomeStatus outcomeStatus;

    public int getIdCrime() {
        return idCrime;
    }

    public void setIdCrime(int idCrime) {
        this.idCrime = idCrime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public String getLocationSubtype() {
        return locationSubtype;
    }

    public void setLocationSubtype(String locationSubtype) {
        this.locationSubtype = locationSubtype;
    }

    public OutcomeStatus getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(OutcomeStatus outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        if (idCrime != crime.idCrime) return false;
        if (category != null ? !category.equals(crime.category) : crime.category != null) return false;
        if (persistentId != null ? !persistentId.equals(crime.persistentId) : crime.persistentId != null) return false;
        if (month != null ? !month.equals(crime.month) : crime.month != null) return false;
        if (location != null ? !location.equals(crime.location) : crime.location != null) return false;
        if (context != null ? !context.equals(crime.context) : crime.context != null) return false;
        if (locationType != crime.locationType) return false;
        if (locationSubtype != null ? !locationSubtype.equals(crime.locationSubtype) : crime.locationSubtype != null)
            return false;
        return outcomeStatus != null ? outcomeStatus.equals(crime.outcomeStatus) : crime.outcomeStatus == null;
    }

    @Override
    public int hashCode() {
        int result = idCrime;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (persistentId != null ? persistentId.hashCode() : 0);
        result = 31 * result + (month != null ? month.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (locationType != null ? locationType.hashCode() : 0);
        result = 31 * result + (locationSubtype != null ? locationSubtype.hashCode() : 0);
        result = 31 * result + (outcomeStatus != null ? outcomeStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Crime.class.getSimpleName() + "[", "]")
                .add("idCrime=" + idCrime)
                .add("category='" + category + "'")
                .add("persistentId='" + persistentId + "'")
                .add("month='" + month + "'")
                .add("location=" + location)
                .add("context='" + context + "'")
                .add("locationType=" + locationType)
                .add("locationSubtype='" + locationSubtype + "'")
                .add("outcomeStatus=" + outcomeStatus)
                .toString();
    }

}