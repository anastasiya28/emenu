package application.dao.interfaces;

public interface Cache<T> {
    T get(Integer id);

    int put(Integer id, T object);
}
