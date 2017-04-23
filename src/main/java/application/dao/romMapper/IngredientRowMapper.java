package application.dao.romMapper;

import application.model.Ingredient;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static application.dao.impls.IngredientDAOImpl.INGREDIENT_ID_COLUMN;
import static application.dao.impls.IngredientDAOImpl.NAME_COLUMN;
import static application.dao.impls.IngredientDAOImpl.TABLE_NAME;

public class IngredientRowMapper implements RowMapper<Ingredient> {
    public static final Logger logger = Logger.getLogger(IngredientRowMapper.class);

    @Override
    public Ingredient mapRow(ResultSet resultSet, int rowNum) {
        Ingredient ingredient = new Ingredient();
        try {
            ingredient.setId(resultSet.getInt(TABLE_NAME + "." + INGREDIENT_ID_COLUMN));
            ingredient.setName(resultSet.getString(TABLE_NAME + "." + NAME_COLUMN));
        } catch (SQLException e) {
            logger.trace("SQLException in the method: public Ingredient mapRow(ResultSet rs, int rowNum) {...} " +
                    "of the class IngredientRowMapper", e);
        }
        return ingredient;
    }
}
