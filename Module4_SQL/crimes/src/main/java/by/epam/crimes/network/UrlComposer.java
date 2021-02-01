package by.epam.crimes.network;

public class UrlComposer {
    private static final String API_METHOD_STREET_LEVEL_CRIME = "https://data.police.uk/api/crimes-street/all-crime";
    private static final String API_METHOD_STOP_AND_SEARCH_BY_FORCE = "https://data.police.uk/api/stops-force";
    private static final String API_METHOD_LIST_OF_FORCES = "https://data.police.uk/api/forces";
    private static final String API_METHOD_AVAILABILITY = "https://data.police.uk/api/crimes-street-dates";

    private UrlComposer() {
    }

    //?lat=52.629729&lng=-1.131592&date=2019-05
    public static String formUrlRequestForStreetLevelCrime(String latitude, String longitude, String date) {
        return String.format("%s?lat=%s&lng=%s&date=%s", API_METHOD_STREET_LEVEL_CRIME, latitude, longitude, date);
    }

    public static String formUrlRequestForListOfForces() {
        return API_METHOD_LIST_OF_FORCES;
    }

    public static String formUrlRequestForMethodAvailability() {
        return API_METHOD_AVAILABILITY;
    }

    //?force=avon-and-somerset&date=2019-05
    public static String formUrlRequestForStopAndSearchByForce(String force, String date) {
        return String.format("%s?force=%s&date=%s", API_METHOD_STOP_AND_SEARCH_BY_FORCE, force, date);
    }

}