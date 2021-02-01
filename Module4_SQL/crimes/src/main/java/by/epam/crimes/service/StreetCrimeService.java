package by.epam.crimes.service;

import by.epam.crimes.dao.impl.StreetCrimeDao;
import by.epam.crimes.entity.Crime;
import by.epam.crimes.exception.JsonInputException;
import by.epam.crimes.exception.ServiceException;
import by.epam.crimes.network.HttpClientJson;
import by.epam.crimes.network.UrlComposer;
import by.epam.crimes.parser.InputDataParser;
import by.epam.crimes.parser.impl.StreetCrimesJsonParser;
import by.epam.crimes.validator.InputDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StreetCrimeService {
    private static final Logger logger = LoggerFactory.getLogger(StreetCrimeService.class);
    private static final String OUTPUT_FILENAME = "SLC_";
    private static final String OUTPUT_EXTENSION = ".txt";

    public boolean saveToFileStreetCrime(String outputDirectory, String inputFile, String date) throws ServiceException {
        boolean result = false;
        File outDir = new File(outputDirectory);
        boolean isExistsDir = outDir.exists();
        if (!isExistsDir) {
            isExistsDir = outDir.mkdir();
            logger.info(String.format("Creation of directory %s is %s", outputDirectory, isExistsDir));
        }
        if (isExistsDir && InputDataValidator.isValidDateFormat(date)) {
            List<String[]> paramsForRequests = InputDataParser.getRequestParams(inputFile);
            for (String[] args : paramsForRequests) {
                if (args.length == 3) {
                    logger.info(String.format("Start getting and writing in file %s for %s", OUTPUT_FILENAME, args[0]));
                    String jsonString = null;
                    try {
                        jsonString = HttpClientJson.getInstance().readStringJsonFromUrl(
                                UrlComposer.formUrlRequestForStreetLevelCrime(args[2], args[1], date));
                        logger.info("Json string has been got");
                    } catch (JsonInputException e) {
                        throw new ServiceException(e);
                    }
                    String fileRegionName = String.format("%s%.20s%s", OUTPUT_FILENAME, args[0], OUTPUT_EXTENSION);
                    File fileRegion = new File(String.format("%s%s%s", outputDirectory, File.separator, fileRegionName));
                    try (FileWriter fileWriter = new FileWriter(fileRegion);
                         BufferedWriter bufferedWriterRegion = new BufferedWriter(fileWriter)) {
                        bufferedWriterRegion.write(jsonString);
                        logger.info(String.format("Data has been written in the file %s", fileRegionName));
                    } catch (IOException ex) {
                        throw new ServiceException(String.format("Error writing in the file %s", fileRegionName));
                    }
                } else {
                    logger.warn("Wrong quantity of parameters");
                }
            }
            result = true;
        }
        return result;
    }

    public boolean saveToDatabaseStreetCrimes(String inputFile, String date) throws ServiceException {
        boolean result = false;
        if (InputDataValidator.isValidDateFormat(date)) {
            List<String[]> paramsForRequests = InputDataParser.getRequestParams(inputFile);
            for (String[] args : paramsForRequests) {
                if (args.length == 3) {
                    logger.info(String.format("Start getting and inserting json for %s", args[0]));
                    try {
                        String jsonString = HttpClientJson.getInstance().readStringJsonFromUrl(
                                UrlComposer.formUrlRequestForStreetLevelCrime(args[2], args[1], date));
                        logger.info("Json string has been got");
                        List<Crime> crimes = StreetCrimesJsonParser.getInstance().readJson(jsonString);
                        logger.info(String.format("Json string has been parsed in size of %d", crimes.size()));
                        StreetCrimeDao.getInstance().saveToDatabase(crimes);
                        logger.info(String.format("End inserting for %s", args[0]));
                    } catch (JsonInputException e) {
                        throw new ServiceException(e);
                    }
                } else {
                    logger.warn("Wrong quantity of parameters");
                }
            }
            result = true;
        }
        return result;
    }

}