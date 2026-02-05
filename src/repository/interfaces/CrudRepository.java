package repository.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    int create(T entity);
    List<T> getAll();
    Optional<T> getById(int id);
    boolean update(int id, T entity);
    boolean delete(int id);
}

