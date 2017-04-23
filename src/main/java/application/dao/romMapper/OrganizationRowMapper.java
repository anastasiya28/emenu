package application.dao.romMapper;

import application.model.Organization;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static application.dao.impls.OrganizationDAOImpl.NAME_COLUMN;
import static application.dao.impls.OrganizationDAOImpl.ORGANIZATION_ID_COLUMN;
import static application.dao.impls.OrganizationDAOImpl.TABLE_NAME;
import static application.dao.impls.OrganizationDAOImpl.INN_COLUMN;
import static application.dao.impls.OrganizationDAOImpl.LEGAL_ADDRESS_COLUMN;

public class OrganizationRowMapper implements RowMapper<Organization> {
    public static final Logger logger = Logger.getLogger(OrganizationRowMapper.class);

    @Override
    public Organization mapRow(ResultSet resultSet, int rowNum) {
        Organization organization = new Organization();
        try {
            organization.setId(resultSet.getInt(TABLE_NAME + "." + ORGANIZATION_ID_COLUMN));
            organization.setName(resultSet.getString(TABLE_NAME + "." + NAME_COLUMN));
            organization.setInn(resultSet.getInt(TABLE_NAME + "." + INN_COLUMN));
            organization.setLegalAddress(resultSet.getString(TABLE_NAME + "." + LEGAL_ADDRESS_COLUMN));
        } catch (SQLException e) {
            logger.trace("SQLException in the method: public Organization mapRow(ResultSet rs, int rowNum) {...} " +
                    "of the class OrganizationRowMapper", e);
        }
        return organization;
    }
}
