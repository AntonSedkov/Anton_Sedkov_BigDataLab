package by.epam.crimes.network;

import by.epam.crimes.exception.JsonInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class HttpClientJson {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientJson.class);
    private static final HttpClientJson INSTANCE = new HttpClientJson();

    private static final int HTTP_TOO_MANY_REQUESTS = 429;

    private HttpClientJson() {
    }

    public static HttpClientJson getInstance() {
        return INSTANCE;
    }

    public String readStringJsonFromUrl(String requestUrl) throws JsonInputException {
        String response = null;
        try {
            URL url = new URL(requestUrl);
            URLConnection connection = url.openConnection();
            int requestCode = ((HttpURLConnection) connection).getResponseCode();
            if (requestCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    response = new String(inputStream.readAllBytes());
                }
            } else if (requestCode == HttpURLConnection.HTTP_UNAVAILABLE) {
                logger.warn("Too much answers: a custom area contains more than 10,000 crimes.");
            } else if (requestCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                logger.warn("GET request longer than 4094 characters, please, use POST");
            } else if (requestCode == HTTP_TOO_MANY_REQUESTS) {
                logger.warn("Too many requests, on average more than 15 requests per second or more than 30 requests per 1 second");
                TimeUnit.SECONDS.sleep(20);
                response = readStringJsonFromUrl(requestUrl);
            } else if (requestCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                logger.warn("Internal server error");
                response = readStringJsonFromUrl(requestUrl);
            } else {
                logger.error("Unpredictable error from server");
                throw new JsonInputException("Unpredictable error from server: code " + requestCode);
            }
        } catch (IOException e) {
            throw new JsonInputException("Bad URL or response value", e);
        } catch (InterruptedException e) {
            throw new JsonInputException("Bad interrupt", e);
        }
        return response;
    }

}