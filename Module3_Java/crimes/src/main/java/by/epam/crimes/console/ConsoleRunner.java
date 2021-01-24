package by.epam.crimes.console;

import by.epam.crimes.exception.ProjectException;
import by.epam.crimes.service.CrimeService;
import by.epam.crimes.service.CrimeServiceImpl;
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
    private static final String OPTION_INPUT_FILE = "Dinputfile";
    private static final String ARG_INPUT_FILE = "inputfile";
    private static final String OPTION_INPUT_FILE_DESC = "path to file with the data";
    private static final String OPTION_DATE = "Dmdate";
    private static final String ARG_DATE = "year-month";
    private static final String OPTION_DATE_DESC = "date in YYYY-MM format for receiving data";
    private static final String OPTION_OUTPUT_DIR = "Doutdir";
    private static final String ARG_OUTPUT_DIR = "outputdir";
    private static final String OPTION_OUTPUT_DIR_DESC = "output directory to save the data";

    private static final String DEFAULT_FILEPATH = "";
    private static final String DEFAULT_DATE = String.format("%4d-%02d", LocalDate.now().getYear() - 1, LocalDate.now().getMonthValue() + 1);
    private static final String DEFAULT_DATE_DESC = "Default date : ";
    private static final String APP_NAME = "Crimes";
    private static final int HELP_MESSAGE_WIDTH = 80;

    public void run(String[] args) {
        logger.info("Start the application Crimes.");
        CrimeService service = new CrimeServiceImpl();
        Options options = createOptions();
        if (args.length > 0) {
            CommandLineParser parser = new DefaultParser();
            try {
                CommandLine cmd = parser.parse(options, args);
                if (cmd.hasOption(OPTION_HELP)) {
                    printHelp(options);
                } else {
                    String inputFile = DEFAULT_FILEPATH;
                    String date = DEFAULT_DATE;
                    String outputDir = DEFAULT_FILEPATH;
                    if (cmd.hasOption(OPTION_INPUT_FILE)) {
                        inputFile = cmd.getOptionValue(OPTION_INPUT_FILE);
                    }
                    if (cmd.hasOption(OPTION_DATE)) {
                        date = cmd.getOptionValue(OPTION_DATE);
                    }
                    if (cmd.hasOption(OPTION_OUTPUT_DIR)) {
                        outputDir = cmd.getOptionValue(OPTION_OUTPUT_DIR);
                    }
                    if (inputFile != null && !inputFile.isBlank()) {
                        boolean result;
                        if (outputDir != null && !outputDir.isBlank()) {
                            if (date != null) {
                                result = service.saveToFile(outputDir, inputFile, date);
                            } else {
                                result = service.saveToFile(outputDir, inputFile, DEFAULT_DATE);
                            }
                            logger.info(String.format("Data has got from %s with date %s and saved to directory %s - %s",
                                    inputFile, date, outputDir, (result) ? "successfully" : "unsuccessfully"));
                        } else {
                            if (date != null) {
                                result = service.saveToDatabase(inputFile, date);
                            } else {
                                result = service.saveToDatabase(inputFile, DEFAULT_DATE);
                            }
                            logger.info(String.format("Data has got from %s with date %s and saved to the database - %s",
                                    inputFile, date, (result) ? "successfully" : "unsuccessfully"));
                        }
                    } else {
                        logger.error("An input file is required.");
                        printHelp(options);
                    }
                }
            } catch (ParseException e) {
                logger.error(String.format("Options parsing exception:%n%s", e));
                printHelp(options);
            } catch (ProjectException e) {
                logger.error(String.format("An exception has occurred:%n%s", e));
            }
        } else {
            logger.error("An input file is required.");
            printHelp(options);
        }
    }

    private Options createOptions() {
        Options options = new Options();
        Option inputFile = Option.builder()
                .longOpt(OPTION_INPUT_FILE)
                .argName(ARG_INPUT_FILE)
                .hasArg()
                .required()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_INPUT_FILE_DESC)
                .build();
        Option date = Option.builder()
                .longOpt(OPTION_DATE)
                .argName(ARG_DATE)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_DATE_DESC)
                .build();
        Option outputDirectory = Option.builder()
                .longOpt(OPTION_OUTPUT_DIR)
                .argName(ARG_OUTPUT_DIR)
                .hasArg()
                .valueSeparator(OPTION_VALUE_SEPARATOR)
                .desc(OPTION_OUTPUT_DIR_DESC)
                .build();
        Option helpOption = new Option(OPTION_HELP, false, OPTION_HELP_DESC);
        OptionGroup groupOne = new OptionGroup();
        groupOne.addOption(inputFile).addOption(helpOption).setRequired(true);
        options.addOptionGroup(groupOne);
        options.addOption(date);
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