package by.epam.crimes.dao;

import by.epam.crimes.entity.Crime;

import java.util.List;

public interface CrimeDao {
    void saveToDatabase(List<Crime> crimes);
}