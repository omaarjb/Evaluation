package ma.projet.dao;

import java.util.List;

public interface IDao<T> {
    T add(T o);
    T update(T o);
    void delete(Long id);
    T getById(Long id);
    List<T> getAll();
}
