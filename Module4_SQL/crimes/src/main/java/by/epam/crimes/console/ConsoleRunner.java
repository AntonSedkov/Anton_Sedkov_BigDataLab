package by.epam.crimes.console;

import by.epam.crimes.exception.ServiceException;
import by.epam.crimes.service.StopAndSearchService;
import by.epam.crimes.service.StreetCrimeService;
import by.epam.crimes.validator.InputDataValidator;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.StringJoiner;

public class ConsoleRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleRunner.class);

    //options
    private static final String OPTION_HELP = "help";
    private static final String OPTION_HELP_DESC = "print help usage";
    private static final char OPTION_VALUE_SEPARATOR = '=';
    private static final String OPTION_INPUT_FILE = "inputfile";
    private static final String ARG_INPUT_FILE = "inputfile";
    private static final String OPTION_INPUT_FILE_DESC = "path to file with the data";
    private static final String OPTION_DATE = "date";
    private static final String ARG_DATE = "year-month";
    private static final String OPTION_DATE_DESC = "date in YYYY-MM format for receiving data";
    private static final String OPTION_OUTPUT_DIR = "outdir";
    private static final String ARG_OUTPUT_DIR = "outputdir";
    private static final String OPTION_OUTPUT_DIR_DESC = "output directory to save the data";
    private static final String OPTION_API_METHOD = "api_method";
    private static final String ARG_API_METHOD = "name";
    private static final String OPTION_API_METHOD_DESC = "target Api method: 'street_crime_db', 'street_crime_file', 'stop_and_search_db', 'stop_and_search_file'";

    private static final String API_STREET_CRIME_DB = "street_crime_db";
    private static final String API_STREET_CRIME_FILE = "street_crime_file";
    private static final String API_STOP_AND_SEARCH_DB = "stop_and_search_db";
    private static final String API_STOP_AND_SEARCH_FILE = "stop_and_search_file";

    private static final String DEFAULT_DIRECTORY = "./def_out";
    private static final String DEFAULT_DATE = String.format("%4d-%02d", LocalDate.now().getYear() - 2, LocalDate.now().getMonthValue() + 1);
    private static final String DEFAULT_DATE_DESC = "Default date : ";
    private static final String APP_NAME = "Crimes";
    private static final int HELP_MESSAGE_WIDTH = 80;

    public void run(String[] args) {
        logger.info("Start the application Crimes.");
        Options options = createOptions();
        if (args.length > 0) {
            CommandLineParser parser = new DefaultParser();
            try {
                CommandLine cmd = parser.parse(options, args);
                if (cmd.hasOption(OPTION_HELP)) {
                    printHelp(options);
                } else {
                    String apiMethod = cmd.getOptionValue(OPTION_API_METHOD);
                    String inputFile = cmd.getOptionValue(OPTION_INPUT_FILE);
                    String date = (cmd.getOptionValue(OPTION_DATE) != null)
                            ? cmd.getOptionValue(OPTION_DATE)
                            : DEFAULT_DATE;
                    String outputDir = (cmd.getOptionValue(OPTION_OUTPUT_DIR) != null)
                            ? cmd.getOptionValue(OPTION_OUTPUT_DIR)
                            : DEFAULT_DIRECTORY;
                    StreetCrimeService streetCrimeService = new StreetCrimeService();
                    StopAndSearchService stopAndSearchService = new StopAndSearchService();
                    boolean result;
                    switch (apiMethod) {
                        case API_STREET_CRIME_DB:
                            if (InputDataValidator.isNonemptyInputFile(inputFile)) {
                                result = streetCrimeService.saveToDatabaseStreetCrimes(inputFile, date);
                                logger.info(String.format("Data has got from %s with date %s and saved to the database - %s",
                                        inputFile, date, (result) ? "successfully" : "unsuccessfully"));
                            } else {
                                logger.error("An input file is required.");
                                printHelp(options);
                            }
                            break;
                        case API_STREET_CRIME_FILE:
                            if (InputDataValidator.isNonemptyInputFile(inputFile)) {
                                result = streetCrimeService.saveToFileStreetCrime(outputDir, inputFile, date);
                                logger.info(String.format("Street crime data has got from %s with date %s and saved to directory %s - %s",
                                        inputFile, date, outputDir, (result) ? "successfully" : "unsuccessfully"));
                            } else {
                                logger.error("An input file is required.");
                                printHelp(options);
                            }
                            break;
                        case API_STOP_AND_SEARCH_DB:
                            result = stopAndSearchService.saveToDatabaseStopAndSearch(date);
                            logger.info(String.format("Stop and search data has been got for date %s and saved to the database - %s",
                                    date, (result) ? "successfully" : "unsuccessfully"));
                            break;
                        case API_STOP_AND_SEARCH_FILE:
                            result = stopAndSearchService.saveToFileStopAndSearch(outputDir, date);
                            logger.info(String.format("Stop and search data has been got for date %s and saved to directory %s - %s",
                                    date, outputDir, (result) ? "successfully" : "unsuccessfully"));
                            break;
                        default:
                            logger.error("Wrong Api method.");
                            printHelp(options);
                    }
                }
            } catch (ParseException e) {
                logger.error(String.format("Options parsing exception:%n%s", e));
                printHelp(options);
            } catch (ServiceException e) {
                logger.error(String.format("An exception has occurred:%n%s", e));
            }
        } else {
            logger.error("Required minimum 1 option for application");
            printHelp(options);
        }
    }

    private Options createOptions() {
        Options options = new Options();
        Option helpOption = new Option(OPTION_HELP, false, OPTION_HELP_DESC);
        Option apiMethod = Option.builder()
                .longOpt(OPTION_API_METHOD)
                .argName(ARG_API_METHOD)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_API_METHOD_DESC)
                .build();
        Option date = Option.builder()
                .longOpt(OPTION_DATE)
                .argName(ARG_DATE)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_DATE_DESC)
                .build();
        Option inputFile = Option.builder()
                .longOpt(OPTION_INPUT_FILE)
                .argName(ARG_INPUT_FILE)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_INPUT_FILE_DESC)
                .build();
        Option outputDirectory = Option.builder()
                .longOpt(OPTION_OUTPUT_DIR)
                .argName(ARG_OUTPUT_DIR)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_OUTPUT_DIR_DESC)
                .build();
        OptionGroup groupOne = new OptionGroup();
        groupOne.addOption(apiMethod).addOption(helpOption).setRequired(true);
        options.addOptionGroup(groupOne);
        options.addOption(date);
        options.addOption(inputFile);
        options.addOption(outputDirectory);
        return options;
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer, HELP_MESSAGE_WIDTH, APP_NAME, options);
        writer.flush();
        formatter.printHelp(APP_NAME, options);
        StringJoiner joiner = new StringJoiner("")
                .add(DEFAULT_DATE_DESC).add(DEFAULT_DATE);
        writer.println(joiner.toString());
        writer.flush();
    }

}