package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository mealRepository;


    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;

    }

    public Meal create(Meal meal, int userID) {
        return mealRepository.save(meal,userID);
    }

    public void delete(int id, int userID) {
        checkNotFoundWithId(mealRepository.delete(id,userID), id);
    }

    public Meal get(int id, int userID) {
        return checkNotFoundWithId(mealRepository.get(id,userID), id);
    }
//
//    public User getByEmail(String email) {
//        return checkNotFound(repository.getByEmail(email), "email=" + email);
//    }

    public List<Meal> getAll(int userID) {
        return (List<Meal>) mealRepository.getAll(userID);
    }

    public List<Meal> getAllByDateTime(int userID, LocalDateTime start,LocalDateTime end) {
      return (List<Meal>) mealRepository.getAllByDate(userID,start,end);
    }
    public List<Meal> getAllByDate(int userID, LocalDate start, LocalDate end) {
        return   getAllByDateTime(userID,LocalDateTime.of(start, LocalTime.MIN),LocalDateTime.of(end, LocalTime.MAX));
    }


    public Meal update(Meal meal,int userID) {
       return checkNotFoundWithId(mealRepository.save(meal,userID), meal.getId());
    }

}