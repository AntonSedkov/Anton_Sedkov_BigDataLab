package by.epam.crimes.parser.impl;

import by.epam.crimes.entity.Availability;
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

public class AvailabilityJsonParser implements UKPoliceJsonParser<Availability> {
    private static final Logger logger = LoggerFactory.getLogger(AvailabilityJsonParser.class);

    private static final String TAG_DATE = "date";
    private static final String TAG_STOP_AND_SEARCH = "stop-and-search";

    private static final AvailabilityJsonParser INSTANCE = new AvailabilityJsonParser();

    private AvailabilityJsonParser() {
    }

    public static AvailabilityJsonParser getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Availability> readJson(String jsonData) throws JsonInputException {
        throw new JsonInputException("Unavailable operation");
    }

    public List<String> parseForcesForDate(String jsonData, String date) throws JsonInputException {
        List<String> forces = new ArrayList<>();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(jsonData)) {
            jsonParser.nextToken();
            logger.info("Starting parsing json for availability and search for date.");
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                Availability availability = parseAvailability(jsonParser);
                if (availability.getDate().equals(date)) {
                    forces = availability.getForces();
                    logger.info(String.format("Find forces for the date %s in quantity of %d forces", date, forces.size()));
                    break;
                }
            }
            if (forces.isEmpty()) {
                logger.warn(String.format("Forces haven't been found for the date %s", date));
            }
        } catch (IOException e) {
            throw new JsonInputException("Parsing problem", e);
        }
        return forces;
    }

    private Availability parseAvailability(JsonParser jsonParser) throws IOException, JsonInputException {
        Availability availability = new Availability();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String name = jsonParser.getCurrentName();
            switch (name) {
                case TAG_DATE:
                    jsonParser.nextToken();
                    availability.setDate(jsonParser.getText());
                    break;
                case TAG_STOP_AND_SEARCH:
                    jsonParser.nextToken();
                    while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                        availability.add(jsonParser.getText());
                    }
                    break;
                default:
                    throw new JsonInputException("Wrong tag for stop and search");
            }
        }
        return availability;
    }

}