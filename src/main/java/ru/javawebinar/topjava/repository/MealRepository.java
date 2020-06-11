package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MealRepository implements Repository<Meal, Integer> {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    {
        MealsUtil.meals.forEach(this::add);
    }
    @Override
    public Meal add(Meal meal) {
        if (meal.isNew()) {
            meal.setID(counter.incrementAndGet());
        }
        return repository.put(meal.getID(),meal);
    }

    @Override
    public boolean delete(Integer i) {
        return repository.remove(i) != null;
    }

    @Override
    public Meal get(Integer i) {
        return repository.get(i);
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream().collect(Collectors.toList());
    }
}
