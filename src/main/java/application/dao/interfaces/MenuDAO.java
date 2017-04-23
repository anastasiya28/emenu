package application.dao.interfaces;

import application.model.Menu;
import application.model.Organization;

import java.util.List;

public interface MenuDAO {
    List<Menu> getAll();

    Menu getById(Integer id);

    List<Menu> getByOrganization(Organization organization);

    Integer add(Menu menu);

    void update(Menu menu);

    void remove(Integer id);
}
