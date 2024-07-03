package main.java.ru.clevertec.check.repositories;

import java.io.IOException;
import java.util.Optional;

public interface Repository<T> {
    Optional<T> findById(String id) throws IOException;
}
