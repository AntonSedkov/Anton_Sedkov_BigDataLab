package by.epam.crimes.console;

import by.epam.crimes.exception.ProjectException;
import by.epam.crimes.service.CrimeService;
import by.epam.crimes.service.CrimeServiceImpl;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Properties;
import java.util.StringJoiner;

public class ConsoleRunner {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleRunner.class);

    private static final String OPTION_JAVA = "D";
    private static final String ARG_FORMAT = "property=value";
    private static final String OPTION_JAVA_DESC = "use value for given properties";
    private static final String OPTION_HELP = "help";
    private static final String OPTION_HELP_DESC = "print help usage";
    private static final String PROPERTY_TITLE = "Properties:\n";
    private static final String PROPERTY_DATE = "date";
    private static final String PROPERTY_DATE_DESC = " - date in YYYY-MM format for receiving data";
    private static final String PROPERTY_INPUT_FILE = "infile";
    private static final String PROPERTY_INPUT_FILE_DESC = " - path to file with the data";
    private static final String PROPERTY_OUTPUT_DIR = "outdir";
    private static final String PROPERTY_OUTPUT_DIR_DESC = " - output directory to save the data";
    private static final String DEFAULT_FILEPATH = "";
    private static final String DEFAULT_DATE = String.format("%4d-%02d", LocalDate.now().getYear() - 1, LocalDate.now().getMonthValue() + 1);
    private static final String DEFAULT_DATE_DESC = "default date : ";
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
                }
                if (cmd.hasOption(OPTION_JAVA)) {
                    Properties properties = cmd.getOptionProperties(OPTION_JAVA);
                    String date = (properties.containsKey(PROPERTY_DATE))
                            ? properties.getProperty(PROPERTY_DATE)
                            : DEFAULT_DATE;
                    String inputFile = (properties.containsKey(PROPERTY_INPUT_FILE))
                            ? properties.getProperty(PROPERTY_INPUT_FILE)
                            : DEFAULT_FILEPATH;
                    String outputDir = (properties.containsKey(PROPERTY_OUTPUT_DIR))
                            ? properties.getProperty(PROPERTY_OUTPUT_DIR)
                            : DEFAULT_FILEPATH;
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
        Option propertyOption = Option.builder()
                .longOpt(OPTION_JAVA)
                .argName(ARG_FORMAT)
                .hasArgs()
                .valueSeparator()
                .numberOfArgs(2)
                .desc(OPTION_JAVA_DESC)
                .build();
        Option helpOption = new Option(OPTION_HELP, false, OPTION_HELP_DESC);
        OptionGroup group = new OptionGroup();
        group.addOption(propertyOption).addOption(helpOption);
        options.addOptionGroup(group);
        return options;
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printUsage(writer, HELP_MESSAGE_WIDTH, APP_NAME, options);
        StringJoiner joiner = new StringJoiner("")
                .add(PROPERTY_TITLE).add(PROPERTY_INPUT_FILE).add(PROPERTY_INPUT_FILE_DESC)
                .add("\n").add(PROPERTY_DATE).add(PROPERTY_DATE_DESC)
                .add("\n").add(DEFAULT_DATE_DESC).add(DEFAULT_DATE)
                .add("\n").add(PROPERTY_OUTPUT_DIR).add(PROPERTY_OUTPUT_DIR_DESC);
        writer.println(joiner.toString());
        writer.flush();
    }

}