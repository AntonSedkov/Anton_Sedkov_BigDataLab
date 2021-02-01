package by.epam.crimes.dao;

import java.util.List;

public interface UKPoliceDao<T> {
    boolean saveToDatabase(List<T> data);
}