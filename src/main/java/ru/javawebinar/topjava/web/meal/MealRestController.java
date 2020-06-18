package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;
import static ru.javawebinar.topjava.util.DateTimeUtil.toTimeOrMaxTime;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private  MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<Meal> getAll() {
        final int userID = authUserId();
        log.info("getAll, user ID {}",userID);
        return service.getAll(userID);
    }

    public List<MealTo> filterByDateTime(String startDate, String endDate, String startTime, String endTime ) {
        final int userID = authUserId();
        log.info("filterByDateTime, user ID {}",userID);
        LocalDate lds = toDateOrMinDate(startDate);
        LocalDate lde = toDateOrMaxDate(endDate);
        LocalTime lts = toTimeOrMinTime(startTime);
        LocalTime lte = toTimeOrMaxTime(endTime);
        final List<Meal> allByDateTime = service.getAllByDate(userID, lds,lde);
        return getFilteredTos(allByDateTime,DEFAULT_CALORIES_PER_DAY,lts,lte);
    }

    public Meal get(int id) {
        final int userID = authUserId();
        log.info("get {}, user ID {}", id,userID);
        return service.get(id, userID);
    }

    public Meal create(Meal meal) {
        final int userID = authUserId();
        log.info("create {}, user ID {}", meal,userID);
        checkNew(meal);
        return service.create(meal,userID);
    }

    public void delete(int id) {
        final int userID = authUserId();
        log.info("delete {}, user ID {}", id,userID);
        service.delete(id,userID);
    }

    public Meal update(Meal meal, int id) {
        final int userID = authUserId();
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
       return service.update(meal,userID);
    }



}