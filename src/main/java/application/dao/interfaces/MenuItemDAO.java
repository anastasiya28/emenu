package application.dao.interfaces;

import application.model.MenuItem;
import application.model.MenuSection;

import java.util.List;

public interface MenuItemDAO {
    List<MenuItem> getAll();

    MenuItem getById(Integer id);

    List<MenuItem> getByName(String name);

    List<MenuItem> getByMenuSection(MenuSection menuSection);

    Integer add(MenuItem menuItem);

    void update(MenuItem menuItem);

    void remove(Integer id);
}
