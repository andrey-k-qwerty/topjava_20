package ru.javawebinar.topjava.repository;

import java.util.List;

public interface Repository<T,N extends Number> {
    T add(T t);  // save, update, edit

    boolean delete(N i);

    T get(N i);

    List<T> getAll();
}
