package by.epam.crimes.parser;

import by.epam.crimes.exception.ProjectException;
import by.epam.crimes.validator.InputDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputDataParser {
    private static final Logger logger = LoggerFactory.getLogger(InputDataParser.class);

    private static final String PARAM_DELIMITER = ",";

    private InputDataParser() {
    }

    public static List<String[]> getRequestParams(String filename) throws ProjectException {
        List<String[]> requests = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            for (String line : lines) {
                if (InputDataValidator.isValidLine(line)) {
                    String[] params = line.split(PARAM_DELIMITER);
                    requests.add(params);
                }
            }
            logger.info("Input file has been parsed successfully.");
        } catch (IOException e) {
            throw new ProjectException("Path is wrong", e);
        }
        return requests;
    }

}