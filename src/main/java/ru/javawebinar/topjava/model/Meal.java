package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractMeal<Integer>{

    public Meal(LocalDateTime dateTime, String description, int calories) {
        super(dateTime, description, calories);
    }

    public Meal(Integer ID, LocalDateTime dateTime, String description, int calories) {
        super(ID, dateTime, description, calories);
    }

    @Override
    public boolean isNew() {
        return   (ID == null || ID.intValue() == 0) ? true : false;
    }


}
