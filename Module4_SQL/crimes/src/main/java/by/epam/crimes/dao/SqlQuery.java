package by.epam.crimes.dao;

public class SqlQuery {
    public static final String INSERT_STREET = "INSERT INTO street(id_street, name_street) VALUES(:id_street, :name_street) ON CONFLICT DO NOTHING";
    public static final String INSERT_LOCATION = "INSERT INTO location(latitude, id_street_fk,longitude) VALUES(:latitude, :id_street_fk, :longitude) ON CONFLICT DO NOTHING";
    public static final String INSERT_STREET_CRIME = "INSERT INTO street_crime(id_crime, category, persistent_id, month, latitude_fk, longitude_fk, context, location_type, location_subtype, category_outcome, date_outcome) " +
            "VALUES(:id_crime, :category, :persistent_id, :month, :latitude_fk, :longitude_fk, :context, :location_type, :location_subtype, :category_outcome, :date_outcome) ON CONFLICT DO NOTHING";

    public static final String INSERT_OUTCOME_OBJECT = "INSERT INTO outcome_object(id_outcome_object, name_outcome_object) VALUES(:id_outcome_object, :name_outcome_object) ON CONFLICT DO NOTHING";
    public static final String INSERT_STOP_AND_SEARCH = "INSERT INTO stop_and_search(force, type, involved_person, datetime, operation, operation_name, latitude_fk, longitude_fk, gender, age_range, " +
            "self_defined_ethnicity, officer_defined_ethnicity, legislation, object_of_search, outcome, outcome_linked_to_object_of_search, removal_of_more_than_outer_clothing, id_outcome_object_fk) " +
            "VALUES(:force, :type, :involved_person, :datetime, :operation, :operation_name, :latitude_fk, :longitude_fk, :gender, :age_range, :self_defined_ethnicity, :officer_defined_ethnicity, " +
            ":legislation, :object_of_search, :outcome, :outcome_linked_to_object_of_search, :removal_of_more_than_outer_clothing, :id_outcome_object_fk) ON CONFLICT DO NOTHING";

    private SqlQuery() {
    }

}