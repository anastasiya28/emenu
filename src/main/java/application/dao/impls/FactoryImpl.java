package application.dao.impls;

import application.dao.interfaces.Factory;
import application.model.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FactoryImpl implements Factory {
    public static final Logger logger = Logger.getLogger(FactoryImpl.class);

    @Override
    public Ingredient getIngredient(Integer id, ResultSet resultSet) {
        Ingredient ingredient = null;
        try {
            ingredient = new Ingredient().setId(id)
                    .setName(resultSet.getString(IngredientDAOImpl.TABLE_NAME + "." + IngredientDAOImpl.NAME_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during Ingredient creation", e);
        }
        return ingredient;
    }

    @Override
    public Measure getMeasure(Integer id, ResultSet resultSet) {
        Measure measure = null;
        try {
            measure = new Measure().setId(id)
                    .setShortName(resultSet.getString(MeasureDAOImpl.TABLE_NAME + "." + MeasureDAOImpl.SHORT_NAME_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during Measure creation", e);
        }
        return measure;
    }

    @Override
    public Organization getOrganization(Integer id, ResultSet resultSet) {
        Organization org = null;
        try {
            org = new Organization().setId(id)
                    .setName(resultSet.getString(OrganizationDAOImpl.TABLE_NAME + "." + OrganizationDAOImpl.NAME_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during Organization creation", e);
        }
        return org;

    }

    @Override
    public Menu getMenu(Integer id, ResultSet resultSet) {
        Menu menu = null;
        try {
            menu = new Menu().setId(id)
                    .setName(resultSet.getString(MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.NAME_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during Menu creation", e);
        }
        return menu;
    }

    @Override
    public MenuSection getMenuSection(Integer id, ResultSet resultSet) {
        MenuSection menuSection = null;
        try {
            menuSection = new MenuSection().setId(id)
                    .setName(resultSet.getString(MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.NAME_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during MenuSection creation", e);
        }
        return menuSection;
    }

    @Override
    public MenuItem getMenuItem(Integer id, ResultSet resultSet) {
        MenuItem menuItem = null;
        try {
            menuItem = new MenuItem().setId(id)
                    .setName(resultSet.getString(MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.NAME_COLUMN))
                    .setPrice(resultSet.getDouble(MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.PRICE_COLUMN));
        } catch (SQLException e) {
            logger.error("SQLException during MenuItem creation", e);
        }
        return menuItem;
    }
}
