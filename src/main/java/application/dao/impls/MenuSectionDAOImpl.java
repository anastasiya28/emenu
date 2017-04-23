package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.Factory;
import application.dao.interfaces.MenuSectionDAO;
import application.model.Menu;
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

@Repository("menuSectionRepository")
public class MenuSectionDAOImpl implements MenuSectionDAO {

    public static final String TABLE_NAME = "menuSection";

    public static final String MENU_SECTION_ID_COLUMN = "menuSectionId";
    public static final String NAME_COLUMN = "name";
    public static final String MENU_ID_COLUMN = "menuId";

    private static final String SQL_FIND_MENU_SECTIONS_WITH_MENU_WITH_MENU_ITEMS = "SELECT " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN + ", " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.NAME_COLUMN + ", " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + ", " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.NAME_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_ITEM_ID_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.NAME_COLUMN + ", " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.PRICE_COLUMN +
            " FROM " + MenuSectionDAOImpl.TABLE_NAME +
            " LEFT JOIN " + MenuDAOImpl.TABLE_NAME +
            " ON " + MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_ID_COLUMN + " = " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN +
            " LEFT JOIN " + MenuItemDAOImpl.TABLE_NAME +
            " ON " + MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN + " = " +
            MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_SECTION_ID_COLUMN;

    private static final String SQL_FIND_BY_ID = SQL_FIND_MENU_SECTIONS_WITH_MENU_WITH_MENU_ITEMS + " WHERE " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN + " = ?";

    private static final String SQL_FIND_BY_NAME = SQL_FIND_MENU_SECTIONS_WITH_MENU_WITH_MENU_ITEMS + " WHERE LOWER(" +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.NAME_COLUMN + ") LIKE LOWER(?)";

    private static final String SQL_FIND_BY_MENU = SQL_FIND_MENU_SECTIONS_WITH_MENU_WITH_MENU_ITEMS + " WHERE " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + " = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME_COLUMN + ", " +
            MENU_ID_COLUMN + ") " + "VALUES (?, ?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME_COLUMN + " = ?" + ", " +
            MENU_ID_COLUMN + " = ?" + " WHERE " + MENU_SECTION_ID_COLUMN + " = ?";

    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + MENU_SECTION_ID_COLUMN + " = ?";

    public static final Logger logger = Logger.getLogger(MenuSectionDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<Menu> menuCache = new CacheImpl<>();
    private Cache<MenuSection> menuSectionCache = new CacheImpl<>();
    private Cache<MenuItem> menuItemCache = new CacheImpl<>();

    @Autowired
    private Factory factory;

    @Autowired
    public MenuSectionDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<MenuSection> getAll() {
        try {
            return (List<MenuSection>) jdbcTemplate.query(SQL_FIND_MENU_SECTIONS_WITH_MENU_WITH_MENU_ITEMS,
                    new MenuSectionResultExtractor());
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public MenuSection getById(Integer id) {
        List<MenuSection> menuSections;
        menuSections = (List<MenuSection>) jdbcTemplate.query(SQL_FIND_BY_ID, new MenuSectionResultExtractor(),
                new Object[]{id});
        if (menuSections == null) return null;
        if (menuSections.size() == 0) return null;
        return menuSections.get(0);
    }

    @Override
    public List<MenuSection> getByName(String name) {
        try {
            return (List<MenuSection>) jdbcTemplate.query(SQL_FIND_BY_NAME, new MenuSectionResultExtractor(),
                    new Object[]{"%" + name + "%"});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<MenuSection> getByMenu(Menu menu) {
        Integer menuId = menu.getId();
        try {
            return (List<MenuSection>) jdbcTemplate.query(SQL_FIND_BY_MENU, new MenuSectionResultExtractor(),
                    new Object[]{menuId});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(MenuSection menuSection) {
        if (menuSection.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                logger.debug("Adding the menuSection to the database. " + menuSection);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{MENU_SECTION_ID_COLUMN});
                preparedStatement.setString(1, menuSection.getName());
                preparedStatement.setInt(2, menuSection.getMenu().getId());
                return preparedStatement;
            }, generatedKeyHolder);
            menuSection.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The menuSection was added to the database successfully. menuSectionId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(MenuSection menuSection) {
        if (menuSection.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the menuSection in the database. " + menuSection);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{MENU_SECTION_ID_COLUMN});
                preparedStatement.setString(1, menuSection.getName());
                preparedStatement.setInt(2, menuSection.getMenu().getId());
                preparedStatement.setInt(3, menuSection.getId());
                return preparedStatement;
            });
            if(menuSectionCache.get(menuSection.getId()) != null){
                menuSectionCache.put(menuSection.getId(), menuSection);
            }
            logger.debug("The menuSection was updated in the database successfully.");
        } else {
            logger.info("There is no menuSection with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the menuSection from the database. menuSectionId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The menuSection was removed from the database successfully. menuSectionId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete menuSection with id: " + id, e);
        }
    }

    private class MenuSectionResultExtractor implements ResultSetExtractor {
        Map<Integer, MenuSection> menuSections = new HashMap<>();

        MenuSection menuSection;
        Menu menu;
        MenuItem menuItem;

        @Override
        public List<MenuSection> extractData(ResultSet resultSet) {
            try {
                while (resultSet.next()) {
                    Integer menuSectionId = resultSet.getInt(MenuSectionDAOImpl.TABLE_NAME + "." +
                            MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN);
                    menuSection = menuSectionCache.get(menuSectionId);
                    if (menuSection == null) {
                        menuSection = factory.getMenuSection(menuSectionId, resultSet);
                        menuSectionCache.put(menuSectionId, menuSection);
                    }

                    Integer menuId = resultSet.getInt(MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN);
                    menu = menuCache.get(menuId);
                    if (menu == null) {
                        menu = factory.getMenu(menuId, resultSet);
                        menuCache.put(menuId, menu);
                    }
                    menuSection.setMenu(menu);

                    Integer menuItemId = resultSet.getInt(MenuItemDAOImpl.TABLE_NAME + "." + MenuItemDAOImpl.MENU_ITEM_ID_COLUMN);
                    menuItem = menuItemCache.get(menuItemId);
                    if (menuItem == null) {
                        menuItem = factory.getMenuItem(menuItemId, resultSet);
                        menuItemCache.put(menuItemId, menuItem);
                    }
                    menuSection.addMenuItem(menuItemId, menuItem);

                    menuSections.put(menuSectionId, menuSection);
                }
            } catch (SQLException e) {
                logger.trace("SQLException", e);
            } catch (DataAccessException e) {
                logger.trace("DataAccessException", e);
            }
            return new ArrayList<>(menuSections.values());
        }
    }
}
