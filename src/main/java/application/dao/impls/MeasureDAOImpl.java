package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.MeasureDAO;
import application.dao.romMapper.MeasureRowMapper;
import application.model.Measure;
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
import java.util.List;

@Repository("measureRepository")
public class MeasureDAOImpl implements MeasureDAO {
    public static final String TABLE_NAME = "measure";

    public static final String MEASURE_ID_COLUMN = "measureId";
    public static final String SHORT_NAME_COLUMN = "shortName";
    public static final String FULL_NAME_COLUMN = "fullName";

    private static final String SQL_FIND_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE " + MEASURE_ID_COLUMN + " = ?";
    private static final String SQL_FIND_BY_SHORT_NAME = SQL_FIND_ALL + " WHERE LOWER(" + SHORT_NAME_COLUMN + ") LIKE LOWER(?)";
    private static final String SQL_FIND_BY_FULL_NAME = SQL_FIND_ALL + " WHERE LOWER(" + FULL_NAME_COLUMN + ") LIKE LOWER(?)";
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + SHORT_NAME_COLUMN + ", " +
            FULL_NAME_COLUMN + ") " + "VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + SHORT_NAME_COLUMN + "  = ?" + ", " +
            FULL_NAME_COLUMN + "  = ?" + " WHERE " + MEASURE_ID_COLUMN + " = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + MEASURE_ID_COLUMN + " = ?";

    private static final Logger logger = Logger.getLogger(MeasureDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<Measure> measureCache = new CacheImpl<>();

    @Autowired
    public MeasureDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Measure> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new MeasureRowMapper());
    }

    @Override
    public Measure getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, new MeasureRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<Measure> getByShortName(String shortName) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_SHORT_NAME, new MeasureRowMapper(), new Object[]{"%" + shortName + "%"});
        } catch (EmptyResultDataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<Measure> getByFullName(String fullName) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_FULL_NAME, new MeasureRowMapper(), new Object[]{"%" + fullName + "%"});
        } catch (EmptyResultDataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(Measure measure) {
        if (measure.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                logger.debug("Adding the measure to the database. " + measure);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{MEASURE_ID_COLUMN});
                preparedStatement.setString(1, measure.getShortName());
                preparedStatement.setString(2, measure.getFullName());
                return preparedStatement;
            }, generatedKeyHolder);
            measure.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The measure was added to the database successfully. measureId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(Measure measure) {
        if (measure.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the measure in the database. " + measure);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{MEASURE_ID_COLUMN});
                preparedStatement.setString(1, measure.getShortName());
                preparedStatement.setString(2, measure.getFullName());
                preparedStatement.setInt(3, measure.getId());
                return preparedStatement;
            });
            if (measureCache.get(measure.getId()) != null) {
                measureCache.put(measure.getId(), measure);
            }
            logger.debug("The measure was updated in the database successfully.");
        } else {
            logger.info("There is no measure with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the measure from the database. measureId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The measure was removed from the database successfully. measureId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete measure with id: " + id, e);
        }
    }
}
