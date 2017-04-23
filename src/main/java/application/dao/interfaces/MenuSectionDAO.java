package application.dao.interfaces;

import application.model.Menu;
import application.model.MenuSection;

import java.util.List;

public interface MenuSectionDAO {
    List<MenuSection> getAll();

    MenuSection getById(Integer id);

    List<MenuSection> getByName(String name);

    List<MenuSection> getByMenu(Menu menu);

    Integer add(MenuSection menuSection);

    void update(MenuSection menuSection);

    void remove(Integer id);
}
