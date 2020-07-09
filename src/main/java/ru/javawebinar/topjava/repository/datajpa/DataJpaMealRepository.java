package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_DATE_TIME = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        final User user = crudRepository.getUserById(userId);
        if (user == null) {
            return null;
        }
        meal.setUser(user);
        if (meal.isNew()) {
            return crudRepository.save(meal);
        } else {
            if (get(meal.getId(), userId) != null) {
                return  crudRepository.save(meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getByIdAndUser_Id(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {

        return crudRepository.getAllByUser_IdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllByUser_IdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(userId,startDateTime,endDateTime);
    }
}
