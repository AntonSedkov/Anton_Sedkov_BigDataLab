package by.epam.crimes.dao.impl;

import by.epam.crimes.connection.ConnectionPool;
import by.epam.crimes.dao.UKPoliceDao;
import by.epam.crimes.entity.Crime;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.query.Query;
import org.codejargon.fluentjdbc.api.query.UpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.epam.crimes.dao.DatabaseColumn.*;
import static by.epam.crimes.dao.SqlQuery.*;

public class StreetCrimeDao implements UKPoliceDao<Crime> {
    private static final Logger logger = LoggerFactory.getLogger(StreetCrimeDao.class);
    private final FluentJdbc fluentJdbc = ConnectionPool.getFluentJdbc();

    private static final StreetCrimeDao INSTANCE = new StreetCrimeDao();

    private StreetCrimeDao() {
    }

    public static StreetCrimeDao getInstance() {
        return INSTANCE;
    }

    public boolean saveToDatabase(List<Crime> crimes) {
        List<Map<String, ?>> namedParamsStreet = transformDataToBatchStreet(crimes);
        List<Map<String, ?>> namedParamsLocation = transformDataToBatchLocation(crimes);
        List<Map<String, ?>> namedParamsCrimes = transformDataToBatchCrime(crimes);
        Query query = fluentJdbc.query();
        logger.info("Starting saving data to database.");
        query.batch(INSERT_STREET).namedParams(namedParamsStreet).run();
        logger.info("Streets data has been saved in database.");
        query.batch(INSERT_LOCATION).namedParams(namedParamsLocation).run();
        logger.info("Locations data has been saved in database.");
        List<UpdateResult> crimeRun = query.batch(INSERT_STREET_CRIME)
                .namedParams(namedParamsCrimes).run();
        logger.info("Street crimes data has been saved in database.");
        return !crimeRun.isEmpty();
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