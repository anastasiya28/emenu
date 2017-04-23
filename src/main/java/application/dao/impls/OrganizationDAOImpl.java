package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.OrganizationDAO;
import application.dao.romMapper.OrganizationRowMapper;
import application.model.Organization;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("organizationRepository")
public class OrganizationDAOImpl implements OrganizationDAO {
    public static final String TABLE_NAME = "organization";

    public static final String ORGANIZATION_ID_COLUMN = "organizationId";
    public static final String NAME_COLUMN = "name";
    public static final String INN_COLUMN = "inn";
    public static final String LEGAL_ADDRESS_COLUMN = "legalAddress";

    private static final String SQL_FIND_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE " + ORGANIZATION_ID_COLUMN + " = ?";
    private static final String SQL_FIND_BY_NAME = SQL_FIND_ALL + " WHERE LOWER(" + NAME_COLUMN + ") LIKE LOWER(?)";
    private static final String SQL_FIND_BY_INN = SQL_FIND_ALL + " WHERE CAST(" + INN_COLUMN + " AS VARCHAR(512)) LIKE ?";
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME_COLUMN + ", " +
            INN_COLUMN + "," + LEGAL_ADDRESS_COLUMN + ") " + "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME_COLUMN + "  = ?" + ", " +
            INN_COLUMN + "  = ?" + ", " + LEGAL_ADDRESS_COLUMN + "  = ?" + " WHERE " + ORGANIZATION_ID_COLUMN + " = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + ORGANIZATION_ID_COLUMN + " = ?";

    public static final Logger logger = Logger.getLogger(OrganizationDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<Organization> orgCache = new CacheImpl<>();

    @Autowired
    public OrganizationDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Organization> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new OrganizationRowMapper());
    }

    @Override
    public Organization getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, new OrganizationRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<Organization> getByName(String name) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_NAME, new OrganizationRowMapper(), new Object[]{"%" + name + "%"});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<Organization> getByINN(String inn) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_INN, new OrganizationRowMapper(),
                    new Object[]{"%" + inn + "%"});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(Organization organization) {
        if (organization.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                logger.debug("Adding the organization to the database. " + organization);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{ORGANIZATION_ID_COLUMN});
                preparedStatement.setString(1, organization.getName());
                preparedStatement.setInt(2, organization.getInn());
                preparedStatement.setString(3, organization.getLegalAddress());
                return preparedStatement;
            }, generatedKeyHolder);
            organization.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The organization was added to the database successfully. organizationId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(Organization organization) {
        if (organization.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the organization in the database. " + organization);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{ORGANIZATION_ID_COLUMN});
                preparedStatement.setString(1, organization.getName());
                preparedStatement.setInt(2, organization.getInn());
                preparedStatement.setString(3, organization.getLegalAddress());
                preparedStatement.setInt(4, organization.getId());
                return preparedStatement;
            });
            if(orgCache.get(organization.getId()) != null){
                orgCache.put(organization.getId(), organization);
            }
            logger.debug("The organization was updated in the database successfully.");
        } else {
            logger.info("There is no organization with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the organization from the database. organizationId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The organization was removed from the database successfully. organizationId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete menu with id: " + id, e);
        }
    }
}
