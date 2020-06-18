package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal,int userID);

    // false if not found
    boolean delete(int id,int userID);

    // null if not found
    Meal get(int id,int userID);

    Collection<Meal> getAll(int userID);

    Collection<Meal> getAllByDate(int userID, LocalDateTime start, LocalDateTime end);
}
