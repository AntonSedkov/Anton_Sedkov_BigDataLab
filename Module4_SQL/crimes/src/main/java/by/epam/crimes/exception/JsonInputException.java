package by.epam.crimes.exception;

public class JsonInputException extends Exception {

    public JsonInputException() {
        super();
    }

    public JsonInputException(String message) {
        super(message);
    }

    public JsonInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonInputException(Throwable cause) {
        super(cause);
    }

}