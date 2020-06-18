package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, meal.isNew() ? authUserId() : meal.getUserID()));
    }


    @Override
    public Meal save(Meal meal, int userID) {
  //      final Meal oldMeal = get(meal.getId(), userID);
        meal.setUserID(userID);
        if (meal.isNew() /*&& oldMeal == null*/) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, mealOld) -> meal);
    }

    @Override
    public boolean delete(int id, int userID) {
        final Meal meal = get(id, userID);
        return meal != null ? repository.remove(id) != null : false;
    }

    @Override
    public Meal get(int id, int userID) {
        final Meal meal = repository.get(id);
        return meal.getUserID() == userID ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userID) {
        return repository.values().stream()
                .filter(meal -> meal.getUserID() == userID)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllByDate(int userID, LocalDateTime start, LocalDateTime end) {
        final List<Meal> collect = repository.values().stream()
                .filter(meal -> meal.getUserID() == userID && isBetweenHalfOpen(meal.getDateTime(),start,end))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        return collect;
    }

}

