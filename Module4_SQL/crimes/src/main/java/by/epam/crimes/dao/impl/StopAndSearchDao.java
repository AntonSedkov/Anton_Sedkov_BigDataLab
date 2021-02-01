package by.epam.crimes.dao.impl;

import by.epam.crimes.connection.ConnectionPool;
import by.epam.crimes.dao.UKPoliceDao;
import by.epam.crimes.entity.StopAndSearch;
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

public class StopAndSearchDao implements UKPoliceDao<StopAndSearch> {
    private static final Logger logger = LoggerFactory.getLogger(StopAndSearchDao.class);
    private final FluentJdbc fluentJdbc = ConnectionPool.getFluentJdbc();

    private static final StopAndSearchDao INSTANCE = new StopAndSearchDao();

    private StopAndSearchDao() {
    }

    public static StopAndSearchDao getInstance() {
        return INSTANCE;
    }

    public boolean saveToDatabase(List<StopAndSearch> stopAndSearches) {
        Query query = fluentJdbc.query();
        List<Map<String, ?>> namedParamsStreet = transformDataToBatchStreet(stopAndSearches);
        List<Map<String, ?>> namedParamsLocation = transformDataToBatchLocation(stopAndSearches);
        List<Map<String, ?>> namedParamsOutcomeObject = transformDataToBatchOutcomeObject(stopAndSearches);
        List<Map<String, ?>> namedParamsStopAndSearches = transformDataToBatchStopAndSearch(stopAndSearches);
        logger.info("Starting saving stop and search data to database.");
        query.batch(INSERT_OUTCOME_OBJECT).namedParams(namedParamsOutcomeObject).run();
        logger.info("Outcome object data has been saved in database.");
        query.batch(INSERT_STREET).namedParams(namedParamsStreet).run();
        logger.info("Streets has been saved in database.");
        query.batch(INSERT_LOCATION).namedParams(namedParamsLocation).run();
        logger.info("Locations has been saved in database.");
        List<UpdateResult> searchRun = query.batch(INSERT_STOP_AND_SEARCH)
                .namedParams(namedParamsStopAndSearches).run();
        logger.info("Stop and search data has been saved in database.");
        return !searchRun.isEmpty();
    }

    private List<Map<String, ?>> transformDataToBatchStreet(List<StopAndSearch> stopAndSearches) {
        List<Map<String, ?>> namedParamsStreet = new ArrayList<>();
        for (StopAndSearch search : stopAndSearches) {
            int id = search.getLocation().getStreet().getIdStreet();
            String name = search.getLocation().getStreet().getNameStreet();
            if (id != 0 && name != null) {
                Map<String, Object> oneStreet = new HashMap<>();
                oneStreet.put(ID_STREET, id);
                oneStreet.put(NAME_STREET, name);
                namedParamsStreet.add(oneStreet);
            }
        }
        return namedParamsStreet;
    }

    private List<Map<String, ?>> transformDataToBatchLocation(List<StopAndSearch> stopAndSearches) {
        List<Map<String, ?>> namedParamsLocation = new ArrayList<>();
        for (StopAndSearch search : stopAndSearches) {
            int idStreet = search.getLocation().getStreet().getIdStreet();
            String latitude = search.getLocation().getLatitude();
            String longitude = search.getLocation().getLongitude();
            if (idStreet != 0 && latitude != null && longitude != null) {
                Map<String, Object> oneLocation = new HashMap<>();
                oneLocation.put(ID_STREET_FK, idStreet);
                oneLocation.put(LATITUDE, latitude);
                oneLocation.put(LONGITUDE, longitude);
                namedParamsLocation.add(oneLocation);
            }
        }
        return namedParamsLocation;
    }

    private List<Map<String, ?>> transformDataToBatchOutcomeObject(List<StopAndSearch> stopAndSearches) {
        List<Map<String, ?>> namedParamsOutcomeObject = new ArrayList<>();
        for (StopAndSearch search : stopAndSearches) {
            String id = search.getOutcomeObject().getIdOutcomeObject();
            String name = search.getOutcomeObject().getNameOutcomeObject();
            if (id != null && name != null) {
                Map<String, Object> oneOutcomeObject = new HashMap<>();
                oneOutcomeObject.put(ID_OUTCOME_OBJECT, id);
                oneOutcomeObject.put(NAME_OUTCOME_OBJECT, name);
                namedParamsOutcomeObject.add(oneOutcomeObject);
            }
        }
        return namedParamsOutcomeObject;
    }

    private List<Map<String, ?>> transformDataToBatchStopAndSearch(List<StopAndSearch> stopAndSearches) {
        List<Map<String, ?>> namedParamsStopAndSearches = new ArrayList<>();
        for (StopAndSearch search : stopAndSearches) {
            Map<String, Object> oneStopAndSearch = new HashMap<>();
            oneStopAndSearch.put(FORCE, search.getForce());
            oneStopAndSearch.put(TYPE, search.getType());
            oneStopAndSearch.put(INVOLVED_PERSON, search.isInvolvedPerson());
            oneStopAndSearch.put(DATETIME, search.getDatetime());
            oneStopAndSearch.put(OPERATION, search.getOperation());
            oneStopAndSearch.put(OPERATION_NAME, search.getOperationName());
            oneStopAndSearch.put(LATITUDE_FK, search.getLocation().getLatitude());
            oneStopAndSearch.put(LONGITUDE_FK, search.getLocation().getLongitude());
            oneStopAndSearch.put(GENDER, search.getGender());
            oneStopAndSearch.put(AGE_RANGE, search.getAgeRange());
            oneStopAndSearch.put(SELF_DEFINED_ETHNICITY, search.getSelfDefinedEthnicity());
            oneStopAndSearch.put(OFFICER_DEFINED_ETHNICITY, search.getOfficerDefinedEthnicity());
            oneStopAndSearch.put(LEGISLATION, search.getLegislation());
            oneStopAndSearch.put(OBJECT_OF_SEARCH, search.getObjectOfSearch());
            oneStopAndSearch.put(OUTCOME, search.getOutcome());
            oneStopAndSearch.put(OUTCOME_LINKED_TO_OBJECT_OF_SEARCH, search.getOutcomeLinkedToObjectOfSearch());
            oneStopAndSearch.put(REMOVAL_OF_MORE_THAN_OUTER_CLOTHING, search.getRemovalOfMoreThanOuterClothing());
            oneStopAndSearch.put(ID_OUTCOME_OBJECT_FK, search.getOutcomeObject().getIdOutcomeObject());
            namedParamsStopAndSearches.add(oneStopAndSearch);
        }
        return namedParamsStopAndSearches;
    }

}