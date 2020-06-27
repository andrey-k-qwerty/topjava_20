package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;


    @Test
    public void get() {
        Meal meal = service.get(START_SEQ_MEAL_ID, USER_ID);
        assertMatch(meal, usersMealsMap.get(USER_ID).get(START_SEQ_MEAL_ID));
    }

    @Test
    public void getAnother() {
        assertThrows(NotFoundException.class, () -> service.get(START_SEQ_MEAL_ID, ADMIN_ID));
    }


    @Test
    public void delete() {
        service.delete(START_SEQ_MEAL_ID, USER_ID);
        assertNull(repository.get(START_SEQ_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteService() {
        service.delete(START_SEQ_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(START_SEQ_MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(1, USER_ID));
    }

    @Test
    public void deletedAnotherMeal() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(START_SEQ_MEAL_ID, ADMIN_ID));
    }


    @Test
    public void getBetweenInclusive() {
        final List<Meal> betweenInclusive = service.getBetweenInclusive(LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 31),
                USER_ID);
        assertMatch(betweenInclusive, getListMeal(USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, getListMeal(USER_ID));
        all = service.getAll(ADMIN_ID);
        assertMatch(all, getListMeal(ADMIN_ID));
    }


    @Test
    public void update() {
        Meal mealUpdate = new Meal(service.get(START_SEQ_MEAL_ID, USER_ID));
        mealUpdate.setCalories(600);
        mealUpdate.setDescription("Update");
        service.update(mealUpdate, USER_ID);
        assertMatch(service.get(START_SEQ_MEAL_ID, USER_ID), mealUpdate);
    }

    @Test
    public void updateAnother() {
        Meal mealUpdate = usersMealsMap.get(USER_ID).get(START_SEQ_MEAL_ID);
        mealUpdate.setCalories(600);
        mealUpdate.setDescription("Update another user");
        assertThrows(NotFoundException.class, () -> service.update(mealUpdate, ADMIN_ID));
    }

    @Test
    public void create() {
        final Meal newMeal = newMealAdmin();
        final Meal createMeal = service.create(new Meal(newMeal), ADMIN_ID);
        newMeal.setId(createMeal.getId());
        assertMatch(createMeal, newMeal);
    }
}