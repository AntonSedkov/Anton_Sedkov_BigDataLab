package by.epam.crimes.parser.impl;

import by.epam.crimes.entity.*;
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

public class StreetCrimesJsonParser implements UKPoliceJsonParser<Crime> {
    private static final Logger logger = LoggerFactory.getLogger(StreetCrimesJsonParser.class);

    private static final String TAG_CATEGORY = "category";
    private static final String TAG_LOCATION_TYPE = "location_type";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_STREET = "street";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_CONTEXT = "context";
    private static final String TAG_OUTCOME_STATUS = "outcome_status";
    private static final String TAG_DATE = "date";
    private static final String TAG_PERSISTENT_ID = "persistent_id";
    private static final String TAG_LOCATION_SUBTYPE = "location_subtype";
    private static final String TAG_MONTH = "month";

    private static final StreetCrimesJsonParser INSTANCE = new StreetCrimesJsonParser();

    private StreetCrimesJsonParser() {
    }

    public static StreetCrimesJsonParser getInstance() {
        return INSTANCE;
    }

    public List<Crime> readJson(String jsonData) throws JsonInputException {
        List<Crime> crimes = new ArrayList<>();
        if (jsonData != null && !jsonData.isBlank() && jsonData.contains(JsonToken.START_OBJECT.asString())) {
            JsonFactory jsonFactory = new JsonFactory();
            try (JsonParser jsonParser = jsonFactory.createParser(jsonData)) {
                //skip start token of json objects array '['
                jsonParser.nextToken();
                logger.info("Starting parsing json for street crimes.");
                //parsing all crimes from input string
                while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                    Crime crime = parseCrimeJson(jsonParser);
                    crimes.add(crime);
                }
                logger.info("End of parsing json for street crimes.");
            } catch (IOException e) {
                throw new JsonInputException("Parsing problem", e);
            }
        }
        return crimes;
    }

    private Crime parseCrimeJson(JsonParser jsonParser) throws IOException, JsonInputException {
        Crime crime = new Crime();
        Location location = new Location();
        Street street = new Street();
        location.setStreet(street);
        OutcomeStatus outcomeStatus = new OutcomeStatus();
        crime.setLocation(location);
        crime.setOutcomeStatus(outcomeStatus);
        //this is top or inside object - for the same tag name
        boolean topObject = true;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String name = jsonParser.getCurrentName();
            switch (name) {
                case TAG_CATEGORY:
                    jsonParser.nextToken();
                    if (topObject) {
                        crime.setCategory(jsonParser.getText());
                    } else {
                        crime.getOutcomeStatus().setCategoryOutcome(jsonParser.getText());
                    }
                    break;
                case TAG_LOCATION_TYPE:
                    jsonParser.nextToken();
                    crime.setLocationType(LocationType.valueOf(jsonParser.getText().toUpperCase()));
                    break;
                case TAG_LOCATION:
                    //skip start tag
                    jsonParser.nextToken();
                    topObject = false;
                    break;
                case TAG_OUTCOME_STATUS:
                    //skip start tag
                    jsonParser.nextToken();
                    if (jsonParser.currentToken() != JsonToken.VALUE_NULL) {
                        topObject = false;
                    }
                    break;
                case TAG_STREET:
                    //skip start tag
                    jsonParser.nextToken();
                    break;
                case TAG_LATITUDE:
                    jsonParser.nextToken();
                    crime.getLocation().setLatitude(jsonParser.getText());
                    break;
                case TAG_LONGITUDE:
                    jsonParser.nextToken();
                    crime.getLocation().setLongitude(jsonParser.getText());
                    //skip end of location
                    jsonParser.nextToken();
                    topObject = true;
                    break;
                case TAG_ID:
                    jsonParser.nextToken();
                    if (topObject) {
                        crime.setIdCrime(jsonParser.getIntValue());
                    } else {
                        crime.getLocation().getStreet().setIdStreet(jsonParser.getIntValue());
                    }
                    break;
                case TAG_NAME:
                    jsonParser.nextToken();
                    crime.getLocation().getStreet().setNameStreet(jsonParser.getText());
                    //skip end tag
                    jsonParser.nextToken();
                    break;
                case TAG_CONTEXT:
                    jsonParser.nextToken();
                    crime.setContext(jsonParser.getText());
                    break;
                case TAG_DATE:
                    jsonParser.nextToken();
                    crime.getOutcomeStatus().setDateOutcome(jsonParser.getText());
                    //skip end tag
                    jsonParser.nextToken();
                    topObject = true;
                    break;
                case TAG_PERSISTENT_ID:
                    jsonParser.nextToken();
                    crime.setPersistentId(jsonParser.getText());
                    break;
                case TAG_LOCATION_SUBTYPE:
                    jsonParser.nextToken();
                    crime.setLocationSubtype(jsonParser.getText());
                    break;
                case TAG_MONTH:
                    jsonParser.nextToken();
                    crime.setMonth(jsonParser.getText());
                    break;
                default:
                    throw new JsonInputException("Wrong tag for stop and search");
            }
        }
        return crime;
    }

}