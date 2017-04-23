package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.Factory;
import application.dao.interfaces.MenuDAO;
import application.model.Menu;
import application.model.MenuSection;
import application.model.Organization;
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

@Repository("menuRepository")
public class MenuDAOImpl implements MenuDAO {
    public static final String TABLE_NAME = "menu";

    public static final String MENU_ID_COLUMN = "menuId";
    public static final String NAME_COLUMN = "name";
    public static final String ORGANIZATION_ID_COLUMN = "organizationId";

    private static final String SQL_FIND_ALL_MENUS_WITH_ORG_WITH_MENU_SECTIONS = " SELECT " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + ", " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.NAME_COLUMN + ", " +
            OrganizationDAOImpl.TABLE_NAME + "." + OrganizationDAOImpl.ORGANIZATION_ID_COLUMN + ", " +
            OrganizationDAOImpl.TABLE_NAME + "." + OrganizationDAOImpl.NAME_COLUMN + ", " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN + ", " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.NAME_COLUMN +
            " FROM " + MenuDAOImpl.TABLE_NAME +
            " LEFT JOIN " + OrganizationDAOImpl.TABLE_NAME +
            " ON " + MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.ORGANIZATION_ID_COLUMN + " = " +
            OrganizationDAOImpl.TABLE_NAME + "." + OrganizationDAOImpl.ORGANIZATION_ID_COLUMN +
            " LEFT JOIN " + MenuSectionDAOImpl.TABLE_NAME +
            " ON " + MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + " = " +
            MenuSectionDAOImpl.TABLE_NAME + "." + MenuSectionDAOImpl.MENU_ID_COLUMN;

    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL_MENUS_WITH_ORG_WITH_MENU_SECTIONS + " WHERE " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + " = ?";

    private static final String SQL_FIND_BY_ORG = SQL_FIND_ALL_MENUS_WITH_ORG_WITH_MENU_SECTIONS + "  WHERE " +
            OrganizationDAOImpl.TABLE_NAME + "." + OrganizationDAOImpl.ORGANIZATION_ID_COLUMN + " = ?";

    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME_COLUMN + ", " +
            ORGANIZATION_ID_COLUMN + ") " + "VALUES (?, ?)";

    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME_COLUMN + " = ?" + ", " +
            ORGANIZATION_ID_COLUMN + " = ?" + " WHERE " + MENU_ID_COLUMN + " = ?";

    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " +
            MenuDAOImpl.TABLE_NAME + "." + MenuDAOImpl.MENU_ID_COLUMN + " = ?";

    private static final Logger logger = Logger.getLogger(MenuDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<Menu> menuCache = new CacheImpl<>();
    private Cache<Organization> orgCache = new CacheImpl<>();
    private Cache<MenuSection> menuSectionCache = new CacheImpl<>();

    @Autowired
    private Factory factory;

    @Autowired
    public MenuDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Menu> getAll() {
        try {
            return (List<Menu>) jdbcTemplate.query(SQL_FIND_ALL_MENUS_WITH_ORG_WITH_MENU_SECTIONS, new MenuResultExtractor());
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Menu getById(Integer id) {
        List<Menu> menuSections;
        menuSections = (List<Menu>) jdbcTemplate.query(SQL_FIND_BY_ID, new MenuResultExtractor(),
                new Object[]{id});
        if (menuSections == null) return null;
        if (menuSections.size() == 0) return null;
        return menuSections.get(0);
    }

    @Override
    public List<Menu> getByOrganization(Organization organization) {
        Integer orgId = organization.getId();
        try {
            return (List<Menu>) jdbcTemplate.query(SQL_FIND_BY_ORG, new MenuResultExtractor(),
                    new Object[]{orgId});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(Menu menu) {
        if (menu.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            logger.debug("Adding the menu to the database. " + menu);
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{MENU_ID_COLUMN});
                preparedStatement.setString(1, menu.getName());
                preparedStatement.setInt(2, menu.getOrganization().getId());
                return preparedStatement;
            }, generatedKeyHolder);
            menu.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The menu was added to the database successfully. menuId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(Menu menu) {
        if (menu.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the menu in the database. " + menu);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{MENU_ID_COLUMN});
                preparedStatement.setString(1, menu.getName());
                preparedStatement.setInt(2, menu.getOrganization().getId());
                preparedStatement.setInt(3, menu.getId());
                return preparedStatement;
            });
            if (menuCache.get(menu.getId()) != null) {
                menuCache.put(menu.getId(), menu);
            }
            logger.debug("The menu was updated in the database successfully.");
        } else {
            logger.info("There is no organization with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the menu from the database. menuId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The menu was removed from the database successfully. menuId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete menu with id: " + id, e);
        }
    }

    private class MenuResultExtractor implements ResultSetExtractor {
        Map<Integer, Menu> menus = new HashMap<>();
        Menu menu;
        Organization org;
        MenuSection menuSection;

        public List<Menu> extractData(ResultSet resultSet) {
            try {
                while (resultSet.next()) {
                    Integer menuId = resultSet.getInt(MenuDAOImpl.TABLE_NAME + "." +
                            MenuDAOImpl.MENU_ID_COLUMN);

                    menu = menuCache.get(menuId);
                    if (menu == null) {
                        menu = factory.getMenu(menuId, resultSet);
                        menuCache.put(menuId, menu);
                    }

                    Integer orgId = resultSet.getInt(OrganizationDAOImpl.TABLE_NAME + "." +
                            OrganizationDAOImpl.ORGANIZATION_ID_COLUMN);
                    org = orgCache.get(orgId);
                    if (org == null) {
                        org = factory.getOrganization(orgId, resultSet);
                        orgCache.put(orgId, org);
                    }
                    menu.setOrganization(org);

                    Integer menuSectionId = resultSet.getInt(MenuSectionDAOImpl.TABLE_NAME + "." +
                            MenuSectionDAOImpl.MENU_SECTION_ID_COLUMN);
                    menuSection = menuSectionCache.get(menuSectionId);
                    if (menuSection == null) {
                        menuSection = factory.getMenuSection(menuSectionId, resultSet);
                        menuSectionCache.put(menuSectionId, menuSection);
                    }
                    menu.addMenuSection(menuSectionId, menuSection);
                    menus.put(menuId, menu);
                }
            } catch (SQLException e) {
                logger.trace("SQLException", e);
            } catch (DataAccessException e) {
                logger.trace("DataAccessException", e);
            }
            return new ArrayList<>(menus.values());
        }
    }
}
