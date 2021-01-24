package by.epam.crimes.network;

import by.epam.crimes.exception.ProjectException;
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

    private static final String API_METHOD_STREET_LEVEL_CRIME = "https://data.police.uk/api/crimes-street/all-crime";
    private static final int HTTP_TOO_MANY_REQUESTS = 429;
    private static final HttpClientJson INSTANCE = new HttpClientJson();

    private HttpClientJson() {
    }

    public static HttpClientJson getInstance() {
        return INSTANCE;
    }

    public String readStringJsonFromApi(String latitude, String longitude, String date) throws ProjectException {
        String requestUrl = String.format("%s?lat=%s&lng=%s&date=%s", API_METHOD_STREET_LEVEL_CRIME, latitude, longitude, date);
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
                TimeUnit.SECONDS.sleep(1);
                response=readStringJsonFromApi(latitude,longitude,date);
            } else if (requestCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                logger.warn("Internal server error");
                response=readStringJsonFromApi(latitude,longitude,date);
            } else {
                logger.warn("Unpredictable error from server");
            }
        } catch (IOException e) {
            throw new ProjectException("Bad URL or response value", e);
        } catch (InterruptedException e) {
            throw new ProjectException("Bad interrupt", e);
        }
        return response;
    }

}