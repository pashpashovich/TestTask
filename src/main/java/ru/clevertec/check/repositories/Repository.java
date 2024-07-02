package main.java.ru.clevertec.check.repositories;

import java.io.IOException;

public interface Repository<T> {
    T findById(String id) throws IOException;
}
