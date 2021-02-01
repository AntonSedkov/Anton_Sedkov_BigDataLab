package by.epam.crimes.dao;

public class DatabaseColumn {

    // sql table 'crime' columns
    public static final String ID_CRIME = "id_crime";
    public static final String CATEGORY = "category";
    public static final String PERSISTENT_ID = "persistent_id";
    public static final String MONTH = "month";
    public static final String LATITUDE_FK = "latitude_fk";
    public static final String LONGITUDE_FK = "longitude_fk";
    public static final String CONTEXT = "context";
    public static final String LOCATION_TYPE = "location_type";
    public static final String LOCATION_SUBTYPE = "location_subtype";
    public static final String CATEGORY_OUTCOME = "category_outcome";
    public static final String DATE_OUTCOME = "date_outcome";

    // sql table 'location' columns
    public static final String LATITUDE = "latitude";
    public static final String ID_STREET_FK = "id_street_fk";
    public static final String LONGITUDE = "longitude";

    // sql table 'street' columns
    public static final String ID_STREET = "id_street";
    public static final String NAME_STREET = "name_street";

    // sql table 'outcome_object' columns
    public static final String ID_OUTCOME_OBJECT = "id_outcome_object";
    public static final String NAME_OUTCOME_OBJECT = "name_outcome_object";

    // sql table 'stop_and_search' columns
    public static final String ID_STOP_AND_SEARCH = "id_stop_and_search";
    public static final String FORCE = "force";
    public static final String TYPE = "type";
    public static final String INVOLVED_PERSON = "involved_person";
    public static final String DATETIME = "datetime";
    public static final String OPERATION = "operation";
    public static final String OPERATION_NAME = "operation_name";
    //latitude_fk
    //longitude_fk
    public static final String GENDER = "gender";
    public static final String AGE_RANGE = "age_range";
    public static final String SELF_DEFINED_ETHNICITY = "self_defined_ethnicity";
    public static final String OFFICER_DEFINED_ETHNICITY = "officer_defined_ethnicity";
    public static final String LEGISLATION = "legislation";
    public static final String OBJECT_OF_SEARCH = "object_of_search";
    public static final String OUTCOME = "outcome";
    public static final String OUTCOME_LINKED_TO_OBJECT_OF_SEARCH = "outcome_linked_to_object_of_search";
    public static final String REMOVAL_OF_MORE_THAN_OUTER_CLOTHING = "removal_of_more_than_outer_clothing";
    public static final String ID_OUTCOME_OBJECT_FK = "id_outcome_object_fk";

    private DatabaseColumn() {
    }

}