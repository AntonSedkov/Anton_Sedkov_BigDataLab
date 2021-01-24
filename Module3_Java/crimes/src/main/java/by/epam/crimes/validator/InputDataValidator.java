package by.epam.crimes.validator;

import java.util.regex.Pattern;

public class InputDataValidator {
    private static final Pattern REQUEST_LINE = Pattern.compile("^[\\w '()\\-\\.\\/]+,[0-9\\.-]+,[0-9\\.-]+$");
    private static final Pattern DATE_FORMAT = Pattern.compile("20[0-9]{2}-([0]{1}[1-9]{1}|[1]{1}[0-2]{1})");

    private InputDataValidator() {
    }

    public static boolean isValidLine(String dataLine) {
        boolean result = dataLine != null && !dataLine.isBlank() && REQUEST_LINE.matcher(dataLine).matches();
        return result;
    }

    public static boolean isValidDateFormat(String date) {
        boolean result = date != null && !date.isBlank() && DATE_FORMAT.matcher(date).matches();
        return result;
    }

}