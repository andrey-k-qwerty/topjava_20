package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int START_SEQ_MEAL_ID = START_SEQ + 2;
    private static final AtomicInteger counter = new AtomicInteger(START_SEQ_MEAL_ID);
    public static final Map<Integer, Map<Integer, Meal>> usersMealsMap = new ConcurrentHashMap<>();

    static {
        MealsUtil.MEALS.forEach(meal -> usersMealsMap.computeIfAbsent(USER_ID, key -> new HashMap<Integer, Meal>()).computeIfAbsent(counter.getAndIncrement(), id -> new Meal(id, meal)));
        usersMealsMap.computeIfAbsent(ADMIN_ID, key -> new HashMap<Integer, Meal>()).computeIfAbsent(counter.getAndIncrement(), id ->
                new Meal(id, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510));
        usersMealsMap.computeIfAbsent(ADMIN_ID, key -> new HashMap<Integer, Meal>()).computeIfAbsent(counter.getAndIncrement(), id ->
                new Meal(id, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500));
    }
    public static List<Meal> getListMeal(int UserId) {
        return usersMealsMap.get(UserId).values().stream().sorted(Comparator.comparing(Meal::getId).reversed()).collect(Collectors.toList());
    }

    public static Meal newMealAdmin() {
       return new Meal(LocalDateTime.of(2020, Month.JUNE, 10, 21, 0), "Админ перекусон", 777);
    }

    public static <T> void  assertMatch(T actual, T expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

}
