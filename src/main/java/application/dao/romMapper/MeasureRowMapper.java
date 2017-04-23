package application.dao.romMapper;

import application.model.Measure;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static application.dao.impls.MeasureDAOImpl.FULL_NAME_COLUMN;
import static application.dao.impls.MeasureDAOImpl.MEASURE_ID_COLUMN;
import static application.dao.impls.MeasureDAOImpl.SHORT_NAME_COLUMN;
import static application.dao.impls.MeasureDAOImpl.TABLE_NAME;

public class MeasureRowMapper implements RowMapper<Measure> {
    public static final Logger logger = Logger.getLogger(MeasureRowMapper.class);

    @Override
    public Measure mapRow(ResultSet resultSet, int rowNum) {
        Measure measure = new Measure();
        try {
            measure.setId(resultSet.getInt(TABLE_NAME + "." + MEASURE_ID_COLUMN));
            measure.setShortName(resultSet.getString(TABLE_NAME + "." + SHORT_NAME_COLUMN));
            measure.setFullName(resultSet.getString(TABLE_NAME + "." + FULL_NAME_COLUMN));
        } catch (SQLException e) {
            logger.trace("SQLException in the method: public Measure mapRow(ResultSet rs, int rowNum) {...} " +
                    "of the class MeasureRowMapper", e);
        }
        return measure;
    }
}
