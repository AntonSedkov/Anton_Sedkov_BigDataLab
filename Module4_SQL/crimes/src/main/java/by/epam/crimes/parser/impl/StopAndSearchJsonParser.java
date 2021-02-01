package by.epam.crimes.parser.impl;

import by.epam.crimes.entity.Location;
import by.epam.crimes.entity.OutcomeObject;
import by.epam.crimes.entity.StopAndSearch;
import by.epam.crimes.entity.Street;
import by.epam.crimes.exception.JsonInputException;
import by.epam.crimes.parser.UKPoliceJsonParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StopAndSearchJsonParser implements UKPoliceJsonParser<StopAndSearch> {
    private static final Logger logger = LoggerFactory.getLogger(StopAndSearchJsonParser.class);

    private static final String TAG_TYPE = "type";
    private static final String TAG_INVOLVED_PERSON = "involved_person";
    private static final String TAG_DATETIME = "datetime";
    private static final String TAG_OPERATION = "operation";
    private static final String TAG_OPERATION_NAME = "operation_name";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_STREET = "street";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_AGE_RANGE = "age_range";
    private static final String TAG_SELF_DEFINED_ETHNICITY = "self_defined_ethnicity";
    private static final String TAG_OFFICER_DEFINED_ETHNICITY = "officer_defined_ethnicity";
    private static final String TAG_LEGISLATION = "legislation";
    private static final String TAG_OBJECT_OF_SEARCH = "object_of_search";
    private static final String TAG_OUTCOME = "outcome";
    private static final String TAG_OUTCOME_LINKED_TO_OBJECT_OF_SEARCH = "outcome_linked_to_object_of_search";
    private static final String TAG_REMOVAL_OF_MORE_THAN_OUTER_CLOTHING = "removal_of_more_than_outer_clothing";
    private static final String TAG_OUTCOME_OBJECT = "outcome_object";

    private static final StopAndSearchJsonParser INSTANCE = new StopAndSearchJsonParser();

    private StopAndSearchJsonParser() {
    }

    public static StopAndSearchJsonParser getInstance() {
        return INSTANCE;
    }

    @Override
    public List<StopAndSearch> readJson(String jsonData) throws JsonInputException {
        List<StopAndSearch> result = new ArrayList<>();
        if (jsonData != null && !jsonData.isBlank() && jsonData.contains(JsonToken.START_OBJECT.asString())) {
            JsonFactory jsonFactory = new JsonFactory();
            try (JsonParser jsonParser = jsonFactory.createParser(jsonData)) {
                //skip start token of json objects array '['
                jsonParser.nextToken();
                logger.info("Starting parsing json for stop and search.");
                //parsing all crimes from input string
                while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                    StopAndSearch search = parseStopAndSearch(jsonParser);
                    result.add(search);
                }
                logger.info("End of parsing json for stop and search.");
            } catch (IOException e) {
                throw new JsonInputException("Parsing problem", e);
            }
        }
        return result;
    }

    private StopAndSearch parseStopAndSearch(JsonParser jsonParser) throws IOException, JsonInputException {
        StopAndSearch stopAndSearch = new StopAndSearch();
        Location location = new Location();
        Street street = new Street();
        location.setStreet(street);
        stopAndSearch.setLocation(location);
        OutcomeObject outcome = new OutcomeObject();
        stopAndSearch.setOutcomeObject(outcome);
        //for the same tag for street and outcome object
        boolean isStreet = false;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String name = jsonParser.getCurrentName();
            switch (name) {
                case TAG_TYPE:
                    jsonParser.nextToken();
                    stopAndSearch.setType(jsonParser.getText());
                    break;
                case TAG_INVOLVED_PERSON:
                    jsonParser.nextToken();
                    stopAndSearch.setInvolvedPerson(jsonParser.getBooleanValue());
                    break;
                case TAG_DATETIME:
                    jsonParser.nextToken();
                    stopAndSearch.setDatetime(jsonParser.getText());
                    break;
                case TAG_OPERATION:
                    jsonParser.nextToken();
                    stopAndSearch.setOperation(jsonParser.getText());
                    break;
                case TAG_OPERATION_NAME:
                    jsonParser.nextToken();
                    stopAndSearch.setOperationName(jsonParser.getText());
                    break;
                case TAG_LOCATION:
                    //skip location tag
                    jsonParser.nextToken();
                    break;
                case TAG_LATITUDE:
                    jsonParser.nextToken();
                    stopAndSearch.getLocation().setLatitude(jsonParser.getText());
                    break;
                case TAG_LONGITUDE:
                    jsonParser.nextToken();
                    stopAndSearch.getLocation().setLongitude(jsonParser.getText());
                    //skip end of location
                    jsonParser.nextToken();
                    break;
                case TAG_STREET:
                    //skip start of street
                    jsonParser.nextToken();
                    isStreet = true;
                    break;
                case TAG_ID:
                    jsonParser.nextToken();
                    if (isStreet) {
                        stopAndSearch.getLocation().getStreet().setIdStreet(jsonParser.getIntValue());
                    } else {
                        stopAndSearch.getOutcomeObject().setIdOutcomeObject(jsonParser.getText());
                    }
                    break;
                case TAG_NAME:
                    jsonParser.nextToken();
                    if (isStreet) {
                        stopAndSearch.getLocation().getStreet().setNameStreet(jsonParser.getText());
                    } else {
                        stopAndSearch.getOutcomeObject().setNameOutcomeObject(jsonParser.getText());
                    }
                    //skip end tag
                    jsonParser.nextToken();
                    break;
                case TAG_GENDER:
                    jsonParser.nextToken();
                    stopAndSearch.setGender(jsonParser.getText());
                    break;
                case TAG_AGE_RANGE:
                    jsonParser.nextToken();
                    stopAndSearch.setAgeRange(jsonParser.getText());
                    break;
                case TAG_SELF_DEFINED_ETHNICITY:
                    jsonParser.nextToken();
                    stopAndSearch.setSelfDefinedEthnicity(jsonParser.getText());
                    break;
                case TAG_OFFICER_DEFINED_ETHNICITY:
                    jsonParser.nextToken();
                    stopAndSearch.setOfficerDefinedEthnicity(jsonParser.getText());
                    break;
                case TAG_LEGISLATION:
                    jsonParser.nextToken();
                    stopAndSearch.setLegislation(jsonParser.getText());
                    break;
                case TAG_OBJECT_OF_SEARCH:
                    jsonParser.nextToken();
                    stopAndSearch.setObjectOfSearch(jsonParser.getText());
                    break;
                case TAG_OUTCOME:
                    jsonParser.nextToken();
                    stopAndSearch.setOutcome(jsonParser.getText());
                    break;
                case TAG_OUTCOME_LINKED_TO_OBJECT_OF_SEARCH:
                    jsonParser.nextToken();
                    stopAndSearch.setOutcomeLinkedToObjectOfSearch(jsonParser.getText());
                    break;
                case TAG_REMOVAL_OF_MORE_THAN_OUTER_CLOTHING:
                    jsonParser.nextToken();
                    stopAndSearch.setRemovalOfMoreThanOuterClothing(
                            Boolean.valueOf(jsonParser.getText()));
                    break;
                case TAG_OUTCOME_OBJECT:
                    //skip start of outcome object
                    jsonParser.nextToken();
                    isStreet = false;
                    break;
                default:
                    throw new JsonInputException("Wrong tag for stop and search");
            }
        }
        return stopAndSearch;
    }

}