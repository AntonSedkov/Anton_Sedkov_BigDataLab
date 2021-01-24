package by.epam.crimes.parser;

import by.epam.crimes.entity.*;
import by.epam.crimes.exception.ProjectException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectJsonParser {
    private static final Logger logger = LoggerFactory.getLogger(ProjectJsonParser.class);

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

    private static final ProjectJsonParser INSTANCE = new ProjectJsonParser();

    private ProjectJsonParser() {
    }

    public static ProjectJsonParser getInstance() {
        return INSTANCE;
    }

    public List<Crime> readJson(String jsonData) throws ProjectException {
        List<Crime> crimes = new ArrayList<>();
        if (jsonData != null && !jsonData.isBlank() && jsonData.contains(JsonToken.START_OBJECT.asString())) {
            JsonFactory jsonFactory = new JsonFactory();
            try (JsonParser jsonParser = jsonFactory.createParser(jsonData)) {
                //skip start token of json objects array '['
                jsonParser.nextToken();
                logger.info("Starting parsing json.");
                //parsing all crimes from input string
                while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                    Crime crime = new Crime();
                    Location location = new Location();
                    Street street = new Street();
                    location.setStreet(street);
                    OutcomeStatus outcomeStatus = new OutcomeStatus();
                    crime.setLocation(location);
                    crime.setOutcomeStatus(outcomeStatus);
                    //this is top or inside object - for the same tag name
                    boolean topObject = true;
                    //parsing one crime
                    parseCrimeJson(jsonParser, crime, topObject);
                    crimes.add(crime);
                }
                logger.info("End of parsing json.");
            } catch (JsonParseException e) {
                throw new ProjectException("Creation Parser or Token Problem", e);
            } catch (IOException e) {
                throw new ProjectException("Parsing problem", e);
            } catch (ClassCastException e) {
                throw new ProjectException("Wrong format data from API", e);
            }
        }
        return crimes;
    }

    private void parseCrimeJson(JsonParser jsonParser, Crime crime, boolean topObject) throws IOException {
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String name = jsonParser.getCurrentName();
            if (TAG_CATEGORY.equals(name)) {
                jsonParser.nextToken();
                if (topObject) {
                    crime.setCategory(jsonParser.getText());
                } else {
                    crime.getOutcomeStatus().setCategoryOutcome(jsonParser.getText());
                }
            } else if (TAG_LOCATION_TYPE.equals(name)) {
                jsonParser.nextToken();
                crime.setLocationType(LocationType.valueOf(jsonParser.getText().toUpperCase()));
            } else if (TAG_LOCATION.equals(name)
                    || TAG_STREET.equals(name)
                    || TAG_OUTCOME_STATUS.equals(name)) {
                jsonParser.nextToken();
                topObject = false;
                //nested object, recursive call
                parseCrimeJson(jsonParser, crime, topObject);
                topObject = true;
            } else if (TAG_LATITUDE.equals(name)) {
                jsonParser.nextToken();
                crime.getLocation().setLatitude(jsonParser.getText());
            } else if (TAG_ID.equals(name)) {
                jsonParser.nextToken();
                if (topObject) {
                    crime.setIdCrime(jsonParser.getIntValue());
                } else {
                    crime.getLocation().getStreet().setIdStreet(jsonParser.getIntValue());
                }
            } else if (TAG_NAME.equals(name)) {
                jsonParser.nextToken();
                crime.getLocation().getStreet().setNameStreet(jsonParser.getText());
            } else if (TAG_LONGITUDE.equals(name)) {
                jsonParser.nextToken();
                crime.getLocation().setLongitude(jsonParser.getText());
            } else if (TAG_CONTEXT.equals(name)) {
                jsonParser.nextToken();
                crime.setContext(jsonParser.getText());
            } else if (TAG_DATE.equals(name)) {
                jsonParser.nextToken();
                crime.getOutcomeStatus().setDateOutcome(jsonParser.getText());
            } else if (TAG_PERSISTENT_ID.equals(name)) {
                jsonParser.nextToken();
                crime.setPersistentId(jsonParser.getText());
            } else if (TAG_LOCATION_SUBTYPE.equals(name)) {
                jsonParser.nextToken();
                crime.setLocationSubtype(jsonParser.getText());
            } else if (TAG_MONTH.equals(name)) {
                jsonParser.nextToken();
                crime.setMonth(jsonParser.getText());
            }
        }
    }

}