package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.Factory;
import application.dao.interfaces.MenuItemDAO;
import application.model.Ingredient;
import application.model.Measure;
import application.model.MenuItem;
import application.model.MenuSection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("menuItemRepository")
public class MenuItemDAOImpl implements MenuItemDAO {
    public static final String TABLE_NAME = "menuItem";
    public static final String INTERMEDIATE_TABLE_NAME = "menuItem_ingredient";

    public static final String MENU_ITEM_ID_COLUMN = "menuItemId";
    public static final String NAME_COLUMN = "name";
    public static final String PORTION_VOLUME_COLUMN = "portionVolume";
    public static final String MEASURE_ID_COLUMN = "measureId";
    public static final String PRICE_COLUMN = "price";
    public static final String MENU_SECTION_ID_COLUMN = "menuSectionId";

    public static final String INTERMEDIATE_MENU_ITEM_ID_COLUMN = "menuItemId";
    public static final String INTERMEDIATE_INGREDIENT_ID_COLUMN = "ingredientId";

    private static final String SQL_FIND_MENU_ITEMS_WITH_MEASURE_WITH_MENU_SECTIONS_WITH_INGREDIENTS = "SELECT " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_ITEM_ID_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.NAME_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.PORTION_VOLUME_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.PRICE_COLUMN + ", " +
            MeasureDAOImpl.TABLE_NAME + "." + MeasureDAOImpl.MEASURE_ID_COLUMN + ", " +
            MeasureDAOImpl.TABLE_NAME + "." + MeasureDAOImpl.SHORT_NAME_COLUMN + ", " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN + ", " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.NAME_COLUMN + ", " +
            IngredientDAOImpl.TABLE_NAME + "." + IngredientDAOImpl.INGREDIENT_ID_COLUMN + ", " +
            IngredientDAOImpl.TABLE_NAME + "." + IngredientDAOImpl.NAME_COLUMN +
            " FROM " + MenuItemDAOImpl.TABLE_NAME +
            " lEFT JOIN " + MeasureDAOImpl.TABLE_NAME +
            " ON " + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MEASURE_ID_COLUMN + " = " +
            MeasureDAOImpl.TABLE_NAME + "." + MeasureDAOImpl.MEASURE_ID_COLUMN +
            " lEFT JOIN " + MenuSectionDAOImpl.TABLE_NAME +
            " ON " + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_SECTION_ID_COLUMN + " = " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN +
            " lEFT JOIN " + MenuItemDAOImpl.INTERMEDIATE_TABLE_NAME +
            " ON " + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_ITEM_ID_COLUMN + " = " +
            MenuItemDAOImpl.INTERMEDIATE_TABLE_NAME + "." + MenuItemDAOImpl.INTERMEDIATE_MENU_ITEM_ID_COLUMN +
            " lEFT JOIN " + IngredientDAOImpl.TABLE_NAME +
            " ON " + MenuItemDAOImpl.INTERMEDIATE_TABLE_NAME + "." + MenuItemDAOImpl.INTERMEDIATE_INGREDIENT_ID_COLUMN +
            " = " + IngredientDAOImpl.TABLE_NAME + "." + IngredientDAOImpl.INGREDIENT_ID_COLUMN;

    private static final String SQL_FIND_BY_ID = SQL_FIND_MENU_ITEMS_WITH_MEASURE_WITH_MENU_SECTIONS_WITH_INGREDIENTS +
            " WHERE " + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_ITEM_ID_COLUMN + " = ?";

    private static final String SQL_FIND_BY_NAME = SQL_FIND_MENU_ITEMS_WITH_MEASURE_WITH_MENU_SECTIONS_WITH_INGREDIENTS +
            " WHERE LOWER(" + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.NAME_COLUMN + ") LIKE LOWER(?)";

    private static final String SQL_FIND_BY_MENU_SECTION = SQL_FIND_MENU_ITEMS_WITH_MEASURE_WITH_MENU_SECTIONS_WITH_INGREDIENTS +
            " WHERE " + MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_SECTION_ID_COLUMN + " = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME_COLUMN + ", " + PRICE_COLUMN +
            ", " + PORTION_VOLUME_COLUMN + ", " + MEASURE_ID_COLUMN + ", " + MENU_SECTION_ID_COLUMN + ") " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME_COLUMN + " = ?" + ", "
            + PRICE_COLUMN + " = ?" + ", " + MEASURE_ID_COLUMN + " = ? " + ", " + MENU_SECTION_ID_COLUMN + " = ?" +
            " WHERE " + MENU_ITEM_ID_COLUMN + " = ?";

    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + MENU_ITEM_ID_COLUMN + " = ?";

    private static final Logger logger = Logger.getLogger(MenuItemDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<MenuItem> menuItemCache = new CacheImpl<>();
    private Cache<Measure> measureCache = new CacheImpl<>();
    private Cache<MenuSection> menuSectionCache = new CacheImpl<>();
    private Cache<Ingredient> ingredientCache = new CacheImpl<>();

    @Autowired
    private Factory factory;

    @Autowired
    public MenuItemDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MenuItem> getAll() {
        try {
            return (List<MenuItem>) jdbcTemplate.query(SQL_FIND_MENU_ITEMS_WITH_MEASURE_WITH_MENU_SECTIONS_WITH_INGREDIENTS,
                    new MenuItemResultExtractor());
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public MenuItem getById(Integer id) {
        List<MenuItem> menuItems;
        menuItems = (List<MenuItem>) jdbcTemplate.query(SQL_FIND_BY_ID, new MenuItemResultExtractor(), new Object[]{id});
        if (menuItems == null) return null;
        if (menuItems.size() == 0) return null;
        return menuItems.get(0);
    }

    @Override
    public List<MenuItem> getByName(String name) {
        try {
            return (List<MenuItem>) jdbcTemplate.query(SQL_FIND_BY_NAME, new MenuItemResultExtractor(),
                    new Object[]{"%" + name + "%"});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<MenuItem> getByMenuSection(MenuSection menuSection) {
        Integer menuSectionId = menuSection.getId();
        try {
            return (List<MenuItem>) jdbcTemplate.query(SQL_FIND_BY_MENU_SECTION, new MenuItemResultExtractor(),
                    new Object[]{menuSectionId});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                logger.debug("Adding the menuItem to the database. " + menuItem);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{MENU_ITEM_ID_COLUMN});
                preparedStatement.setString(1, menuItem.getName());
                preparedStatement.setDouble(2, menuItem.getPrice());
                preparedStatement.setInt(3, menuItem.getPortionVolume());
                preparedStatement.setInt(4, menuItem.getMeasure().getId());
                preparedStatement.setInt(5, menuItem.getMenuSection().getId());
                return preparedStatement;
            }, generatedKeyHolder);
            menuItem.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The menuItem was added to the database successfully. menuItemId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(MenuItem menuItem) {
        if (menuItem.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the menuItem in the database. " + menuItem);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{MENU_ITEM_ID_COLUMN});
                preparedStatement.setString(1, menuItem.getName());
                preparedStatement.setDouble(2, menuItem.getPrice());
                preparedStatement.setInt(3, menuItem.getMeasure().getId());
                preparedStatement.setInt(4, menuItem.getMenuSection().getId());
                preparedStatement.setInt(5, menuItem.getId());
                return preparedStatement;
            });
            if(menuItemCache.get(menuItem.getId()) != null){
                menuItemCache.put(menuItem.getId(), menuItem);
            }
            logger.debug("The menuItem was updated in the database successfully.");
        } else {
            logger.info("There is no menuItem with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the menuItem from the database. menuItemId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The menuItem was removed from the database successfully. menuItemId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete menuItem with id: " + id, e);
        }
    }

    private class MenuItemResultExtractor implements ResultSetExtractor {
        Map<Integer, MenuItem> menuItems = new HashMap<>();

        MenuItem menuItem;
        Measure measure;
        MenuSection menuSection;
        Ingredient ingredient;

        @Override
        public List<MenuItem> extractData(ResultSet resultSet) {
            try {
                while (resultSet.next()) {
                    Integer menuItemId = resultSet.getInt(MenuItemDAOImpl.TABLE_NAME + "." +
                            MenuItemDAOImpl.MENU_ITEM_ID_COLUMN);
                    menuItem = menuItemCache.get(menuItemId);
                    if (menuItem == null) {
                        menuItem = factory.getMenuItem(menuItemId, resultSet);
                        menuItemCache.put(menuItemId, menuItem);
                    }

                    Integer measureId = resultSet.getInt(MeasureDAOImpl.TABLE_NAME + "." +
                            MeasureDAOImpl.MEASURE_ID_COLUMN);
                    measure = measureCache.get(measureId);
                    if (measure == null) {
                        measure = factory.getMeasure(measureId, resultSet);
                        measureCache.put(measureId, measure);
                    }
                    menuItem.setMeasure(measure);

                    Integer menuSectionId = resultSet.getInt(MenuSectionDAOImpl.TABLE_NAME + "." +
                            MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN);
                    menuSection = menuSectionCache.get(menuSectionId);
                    if (menuSection == null) {
                        menuSection = factory.getMenuSection(menuSectionId, resultSet);
                        menuSectionCache.put(menuSectionId, menuSection);
                    }
                    menuItem.setMenuSection(menuSection);

                    Integer ingredientId = resultSet.getInt(IngredientDAOImpl.TABLE_NAME + "." +
                            IngredientDAOImpl.INGREDIENT_ID_COLUMN);
                    ingredient = ingredientCache.get(ingredientId);
                    if (ingredient == null) {
                        ingredient = factory.getIngredient(ingredientId, resultSet);
                        ingredientCache.put(ingredientId, ingredient);
                    }
                    menuItem.addIngredient(ingredientId, ingredient);

                    menuItems.put(menuItemId, menuItem);
                }
            } catch (SQLException e) {
                logger.trace("SQLException", e);
            } catch (DataAccessException e) {
                logger.trace("DataAccessException", e);
            }
            return new ArrayList<>(menuItems.values());
        }
    }
}
