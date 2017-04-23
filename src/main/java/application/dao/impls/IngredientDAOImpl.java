package application.dao.impls;

import application.dao.interfaces.Cache;
import application.dao.interfaces.IngredientDAO;
import application.dao.romMapper.IngredientRowMapper;
import application.model.Ingredient;
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

@Repository("ingredientRepository")
public class IngredientDAOImpl implements IngredientDAO {
    public static final String TABLE_NAME = "ingredient";

    public static final String INGREDIENT_ID_COLUMN = "ingredientId";
    public static final String NAME_COLUMN = "name";

    private static final String SQL_FIND_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " WHERE " + INGREDIENT_ID_COLUMN + " = ?";
    private static final String SQL_FIND_BY_NAME = SQL_FIND_ALL + " WHERE LOWER(" + NAME_COLUMN + ") LIKE LOWER(?)";
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME + " (" + NAME_COLUMN + ") " + "VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE " + TABLE_NAME + " SET " + NAME_COLUMN + "  = ?" + " WHERE " +
            INGREDIENT_ID_COLUMN + " = ?";
    private static final String SQL_DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + INGREDIENT_ID_COLUMN + " = ?";

    private static final Logger logger = Logger.getLogger(IngredientDAOImpl.class);
    private JdbcTemplate jdbcTemplate;

    private Cache<Ingredient> ingredientCache = new CacheImpl<>();

    @Autowired
    public IngredientDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Ingredient> getAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, new IngredientRowMapper());
    }

    @Override
    public Ingredient getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{id}, new IngredientRowMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public List<Ingredient> getByName(String name) {
        try {
            return jdbcTemplate.query(SQL_FIND_BY_NAME, new IngredientRowMapper(),
                    new Object[]{"%" + name + "%"});
        } catch (DataAccessException e) {
            logger.trace("Empty result", e);
        }
        return null;
    }

    @Override
    public Integer add(Ingredient ingredient) {
        if (ingredient.getId() == null) {
            KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                logger.debug("Adding the ingredient to the database. " + ingredient);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT,
                        new String[]{INGREDIENT_ID_COLUMN});
                preparedStatement.setString(1, ingredient.getName());
                return preparedStatement;
            }, generatedKeyHolder);
            ingredient.setId((Integer) generatedKeyHolder.getKey());
            logger.debug("The ingredient was added to the database successfully. ingredientId: " + generatedKeyHolder.getKey());
            return (Integer) generatedKeyHolder.getKey();
        } else {
            return -1;
        }
    }

    @Override
    public void update(Ingredient ingredient) {
        if (ingredient.getId() != null) {
            jdbcTemplate.update(connection -> {
                logger.debug("Updating the ingredient in the database. " + ingredient);
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE,
                        new String[]{INGREDIENT_ID_COLUMN});
                preparedStatement.setString(1, ingredient.getName());
                preparedStatement.setInt(2, ingredient.getId());
                return preparedStatement;
            });
            if (ingredientCache.get(ingredient.getId()) != null) {
                ingredientCache.put(ingredient.getId(), ingredient);
            }
            logger.debug("The ingredient was updated in the database successfully.");
        } else {
            logger.info("There is no ingredient with such id in the database");
        }
    }

    @Override
    public void remove(Integer id) {
        try {
            logger.debug("Removing the ingredient from the database. ingredientId: " + id);
            jdbcTemplate.update(SQL_DELETE, id);
            logger.debug("The ingredient was removed from the database successfully. ingredientId: " + id);
        } catch (DataAccessException e) {
            logger.trace("Error during delete ingredient with id: " + id, e);
        }
    }
}
