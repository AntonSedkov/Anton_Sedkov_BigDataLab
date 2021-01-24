package by.epam.crimes.service;

import by.epam.crimes.exception.ProjectException;

public interface CrimeService {

    boolean saveToDatabase(String inputFile, String date) throws ProjectException;

    boolean saveToFile(String outputDirectory, String inputFile, String date) throws ProjectException;

}