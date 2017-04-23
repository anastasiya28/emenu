package application.dao.impls;

import application.dao.interfaces.Cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheImpl<T> implements Cache<T> {
    private int maxLength = 1000;

    private int iterator = 0;

    private final Map<Integer, T> map = new LinkedHashMap<>();
    private int mapSize = 0;

    private int[] keys = new int[maxLength];

    @Override
    public T get(Integer id) {
        return map.get(id);
    }

    @Override
    public int put(Integer id, T object) {
        //если кэш полностью заполнен, удаляем наиболе старые объекты.
        if (iterator >= maxLength) iterator = 0;
        if (mapSize >= maxLength) {
            T removedObject = map.remove(keys[iterator]);
            if (removedObject != null) mapSize--;
        }

        map.put(id, object);
        keys[iterator] = id;
        mapSize++;
        iterator++;
        return id;
    }
}