package by.epam.crimes.service;

import by.epam.crimes.dao.impl.StopAndSearchDao;
import by.epam.crimes.entity.StopAndSearch;
import by.epam.crimes.exception.JsonInputException;
import by.epam.crimes.exception.ServiceException;
import by.epam.crimes.network.HttpClientJson;
import by.epam.crimes.network.UrlComposer;
import by.epam.crimes.parser.impl.AvailabilityJsonParser;
import by.epam.crimes.parser.impl.StopAndSearchJsonParser;
import by.epam.crimes.validator.InputDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StopAndSearchService {
    private static final Logger logger = LoggerFactory.getLogger(StopAndSearchService.class);
    private static final String OUTPUT_FILENAME = "SAS_";
    private static final String OUTPUT_EXTENSION = ".txt";

    public boolean saveToFileStopAndSearch(String outputDirectory, String date) throws ServiceException {
        boolean result = false;
        File outDir = new File(outputDirectory);
        boolean isExistsDir = outDir.exists();
        if (!isExistsDir) {
            isExistsDir = outDir.mkdir();
            logger.info(String.format("Creation of directory %s is %s", outputDirectory, isExistsDir));
        }
        if (isExistsDir && InputDataValidator.isValidDateFormat(date)) {
            try {
                List<String> forces = getParsedForces(date);
                for (String force : forces) {
                    String stops = getJsonStopAndSearchByForce(force, date);
                    File fileForce = new File(String.format("%s%s%s", outputDirectory, File.separator,
                            String.format("%s%.20s%s", OUTPUT_FILENAME, force, OUTPUT_EXTENSION)));
                    try (FileWriter fileWriter = new FileWriter(fileForce);
                         BufferedWriter bufferedWriterRegion = new BufferedWriter(fileWriter)) {
                        bufferedWriterRegion.write(stops);
                        logger.info(String.format("Data has been written in the file %s", fileForce));
                    } catch (IOException ex) {
                        throw new ServiceException(String.format("Error writing in the file %s", fileForce));
                    }
                }
            } catch (JsonInputException e) {
                throw new ServiceException(e);
            }
            result = true;
        }
        return result;
    }

    public boolean saveToDatabaseStopAndSearch(String date) throws ServiceException {
        boolean result = false;
        if (InputDataValidator.isValidDateFormat(date)) {
            try {
                List<String> forces = getParsedForces(date);
                for (String force : forces) {
                    String jsonString = getJsonStopAndSearchByForce(force, date);
                    List<StopAndSearch> stops = getParsedStopAndSearch(jsonString);
                    for (StopAndSearch stop : stops) {
                        stop.setForce(force);
                    }
                    result = StopAndSearchDao.getInstance().saveToDatabase(stops);
                    logger.info(String.format("End inserting for force %s with result %s", force, result));
                }
                logger.info(String.format("All data has been saved for date %s", date));
            } catch (JsonInputException e) {
                throw new ServiceException(e);
            }
        }
        return result;
    }

    private List<String> getParsedForces(String date) throws JsonInputException {
        logger.info(String.format("Start getting forces from availability json for %s", date));
        String jsonForcesString = HttpClientJson.getInstance().readStringJsonFromUrl(UrlComposer.formUrlRequestForMethodAvailability());
        logger.info("Json availability string has been got");
        List<String> forces = AvailabilityJsonParser.getInstance().parseForcesForDate(jsonForcesString, date);
        logger.info(String.format("Forces has been parsed in size of %d", forces.size()));
        return forces;
    }

    private String getJsonStopAndSearchByForce(String force, String date) throws JsonInputException {
        logger.info(String.format("Start getting stop and search json for %s", force));
        String jsonStopsString = HttpClientJson.getInstance().readStringJsonFromUrl(
                UrlComposer.formUrlRequestForStopAndSearchByForce(force, date));
        logger.info("Json stop and search string has been got");
        return jsonStopsString;
    }

    private List<StopAndSearch> getParsedStopAndSearch(String jsonStopsString) throws JsonInputException {
        logger.info("Json stop and search string is parsing");
        List<StopAndSearch> stops = StopAndSearchJsonParser.getInstance().readJson(jsonStopsString);
        logger.info(String.format("Stop and search has been parsed in size of %d", stops.size()));
        return stops;
    }

}