package by.epam.crimes.dao;

import by.epam.crimes.entity.Crime;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrimeDaoImpl implements CrimeDao {
    private static final Logger logger = LoggerFactory.getLogger(CrimeDaoImpl.class);

    //Setting up HikariCP
    private static final HikariConfig config = new HikariConfig("/datasource.properties");
    private static final HikariDataSource dataSource = new HikariDataSource(config);
    //Setting up FluentJdbc
    private static final FluentJdbc fluentJdbc = new FluentJdbcBuilder().connectionProvider(dataSource).build();

    //SQL Queries
    public static final String INSERT_STREET = "INSERT INTO street(id_street, name_street) VALUES(:id_street, :name_street) ON CONFLICT DO NOTHING";
    public static final String INSERT_LOCATION = "INSERT INTO location(latitude, id_street_fk,longitude) VALUES(:latitude, :id_street_fk, :longitude) ON CONFLICT DO NOTHING";
    public static final String INSERT_CRIME = "INSERT INTO crime(id_crime, category, persistent_id, month, latitude_fk, longitude_fk, context, location_type, location_subtype, category_outcome, date_outcome) " +
            "VALUES(:id_crime, :category, :persistent_id, :month, :latitude_fk, :longitude_fk, :context, :location_type, :location_subtype, :category_outcome, :date_outcome) ON CONFLICT DO NOTHING";

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

    private static final CrimeDaoImpl INSTANCE = new CrimeDaoImpl();

    private CrimeDaoImpl() {
    }

    public static CrimeDaoImpl getInstance() {
        return INSTANCE;
    }

    public void saveToDatabase(List<Crime> crimes) {
        List<Map<String, ?>> namedParamsStreet = transformDataToBatchStreet(crimes);
        List<Map<String, ?>> namedParamsLocation = transformDataToBatchLocation(crimes);
        List<Map<String, ?>> namedParamsCrimes = transformDataToBatchCrime(crimes);
        Query query = fluentJdbc.query();
        logger.info("Starting saving data to database.");
        query.batch(INSERT_STREET)
                .namedParams(namedParamsStreet)
                .run();
        logger.info("Streets data has been saved in database.");
        query.batch(INSERT_LOCATION)
                .namedParams(namedParamsLocation)
                .run();
        logger.info("Locations data has been saved in database.");
        query.batch(INSERT_CRIME)
                .namedParams(namedParamsCrimes)
                .run();
        logger.info("Crimes data has been saved in database.");
    }

    private List<Map<String, ?>> transformDataToBatchStreet(List<Crime> crimes) {
        List<Map<String, ?>> namedParamsStreet = new ArrayList<>();
        for (Crime crime : crimes) {
            Map<String, Object> oneStreet = new HashMap<>();
            oneStreet.put(ID_STREET, crime.getLocation().getStreet().getIdStreet());
            oneStreet.put(NAME_STREET, crime.getLocation().getStreet().getNameStreet());
            namedParamsStreet.add(oneStreet);
        }
        return namedParamsStreet;
    }

    private List<Map<String, ?>> transformDataToBatchLocation(List<Crime> crimes) {
        List<Map<String, ?>> namedParamsLocation = new ArrayList<>();
        for (Crime crime : crimes) {
            Map<String, Object> oneLocation = new HashMap<>();
            oneLocation.put(LATITUDE, crime.getLocation().getLatitude());
            oneLocation.put(ID_STREET_FK, crime.getLocation().getStreet().getIdStreet());
            oneLocation.put(LONGITUDE, crime.getLocation().getLongitude());
            namedParamsLocation.add(oneLocation);
        }
        return namedParamsLocation;
    }

    private List<Map<String, ?>> transformDataToBatchCrime(List<Crime> crimes) {
        List<Map<String, ?>> namedParamsCrimes = new ArrayList<>();
        for (Crime crime : crimes) {
            Map<String, Object> oneCrime = new HashMap<>();
            oneCrime.put(ID_CRIME, crime.getIdCrime());
            oneCrime.put(CATEGORY, crime.getCategory());
            oneCrime.put(PERSISTENT_ID, crime.getPersistentId());
            oneCrime.put(MONTH, crime.getMonth());
            oneCrime.put(LATITUDE_FK, crime.getLocation().getLatitude());
            oneCrime.put(LONGITUDE_FK, crime.getLocation().getLongitude());
            oneCrime.put(CONTEXT, crime.getContext());
            oneCrime.put(LOCATION_TYPE, crime.getLocationType().toString());
            oneCrime.put(LOCATION_SUBTYPE, crime.getLocationSubtype());
            oneCrime.put(CATEGORY_OUTCOME, crime.getOutcomeStatus().getCategoryOutcome());
            oneCrime.put(DATE_OUTCOME, crime.getOutcomeStatus().getDateOutcome());
            namedParamsCrimes.add(oneCrime);
        }
        return namedParamsCrimes;
    }

}