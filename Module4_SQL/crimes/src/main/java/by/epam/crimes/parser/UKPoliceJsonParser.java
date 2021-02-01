package by.epam.crimes.parser;

import by.epam.crimes.exception.JsonInputException;

import java.util.List;

public interface UKPoliceJsonParser<T> {
    List<T> readJson(String jsonData) throws JsonInputException;
}