package by.epam.crimes.service;

import by.epam.crimes.dao.CrimeDao;
import by.epam.crimes.dao.CrimeDaoImpl;
import by.epam.crimes.entity.Crime;
import by.epam.crimes.exception.ProjectException;
import by.epam.crimes.network.HttpClientJson;
import by.epam.crimes.parser.InputDataParser;
import by.epam.crimes.parser.ProjectJsonParser;
import by.epam.crimes.validator.InputDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CrimeServiceImpl implements CrimeService {
    private static final Logger logger = LoggerFactory.getLogger(CrimeServiceImpl.class);
    private static final String OUTPUT_FILENAME = "SLC";
    private static final String OUTPUT_EXTENSION = ".txt";

    public boolean saveToFile(String outputDirectory, String inputFile, String date) throws ProjectException {
        boolean result = false;
        File outDir = new File(outputDirectory);
        boolean isExistsDir = outDir.exists();
        if (!isExistsDir) {
            isExistsDir = outDir.mkdir();
            logger.info(String.format("Creation of directory %s is %s", outputDirectory, isExistsDir));
        }
        if (isExistsDir && inputFile != null && !inputFile.isBlank() && date != null && !date.isBlank() && InputDataValidator.isValidDateFormat(date)) {
            List<String[]> paramsForRequests = InputDataParser.getRequestParams(inputFile);
            for (String[] args : paramsForRequests) {
                if (args.length == 3) {
                    logger.info(String.format("Start getting and writing in file %s for %s", OUTPUT_FILENAME, args[0]));
                    String latitude = args[2];
                    String longitude = args[1];
                    String jsonString = HttpClientJson.getInstance().readStringJsonFromApi(latitude, longitude, date);
                    logger.info("Json string has been got");
                    String fileRegionName = String.format("%s%.20s%s", OUTPUT_FILENAME, args[0], OUTPUT_EXTENSION);
                    File fileRegion = new File(String.format("%s%s%s", outputDirectory, File.separator, fileRegionName));
                    try (FileWriter fileWriter = new FileWriter(fileRegion);
                         BufferedWriter bufferedWriterRegion = new BufferedWriter(fileWriter)) {
                        bufferedWriterRegion.write(jsonString);
                        logger.info(String.format("Data has been written in the file %s", fileRegionName));
                    } catch (IOException ex) {
                        throw new ProjectException(String.format("Error writing in the file %s", fileRegionName));
                    }
                }
            }
            result = true;
        }
        return result;
    }

    public boolean saveToDatabase(String inputFile, String date) throws ProjectException {
        boolean result = false;
        if (inputFile != null && !inputFile.isBlank() && date != null && !date.isBlank() && InputDataValidator.isValidDateFormat(date)) {
            List<String[]> paramsForRequests = InputDataParser.getRequestParams(inputFile);
            for (String[] args : paramsForRequests) {
                if (args.length == 3) {
                    logger.info(String.format("Start getting and inserting json for %s", args[0]));
                    String latitude = args[2];
                    String longitude = args[1];
                    String jsonString = HttpClientJson.getInstance().readStringJsonFromApi(latitude, longitude, date);
                    logger.info("Json string has been got");
                    List<Crime> crimes = ProjectJsonParser.getInstance().readJson(jsonString);
                    logger.info(String.format("Json string has been parsed in size of %d", crimes.size()));
                    CrimeDao dao = CrimeDaoImpl.getInstance();
                    dao.saveToDatabase(crimes);
                    logger.info(String.format("End inserting for %s", args[0]));
                } else {
                    logger.warn("Wrong quantity of parameters");
                }
            }
            result = true;
        }
        return result;
    }

   /* public static void main(String[] args) throws ProjectException {
        boolean res = new CrimeServiceImpl()
                .saveToDatabase("E:\\Data Lab\\Anton_Sedkov_BigDataLab\\Module3_Java\\crimes\\input\\LondonStations.csv",
                "2020-10");
    }*/

    /*public static void main(String[] args) throws ProjectException {
        boolean res = new CrimeServiceImpl()
                .saveToFile(
                        "E:\\Data Lab\\Anton_Sedkov_BigDataLab\\Module3_Java\\crimes\\output",
                        "E:\\Data Lab\\Anton_Sedkov_BigDataLab\\Module3_Java\\crimes\\input\\LondonStations.csv",
                        "2020-10");
    }*/

}