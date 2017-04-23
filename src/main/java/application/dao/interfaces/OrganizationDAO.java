package application.dao.interfaces;

import application.model.Organization;

import java.util.List;

public interface OrganizationDAO {
    List<Organization> getAll();

    Organization getById(Integer id);

    List<Organization> getByName(String name);

    List<Organization> getByINN(String inn);

    Integer add(Organization organization);

    void update(Organization organization);

    void remove(Integer id);
}
