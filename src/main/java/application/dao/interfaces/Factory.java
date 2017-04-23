package application.dao.interfaces;

import application.model.*;

import java.sql.ResultSet;

public interface Factory {
    Ingredient getIngredient(Integer id, ResultSet resultSet);

    Measure getMeasure(Integer id, ResultSet resultSet);

    Organization getOrganization(Integer id, ResultSet resultSet);

    Menu getMenu(Integer id, ResultSet resultSet);

    MenuSection getMenuSection(Integer id, ResultSet resultSet);

    MenuItem getMenuItem(Integer id, ResultSet resultSet);
}
