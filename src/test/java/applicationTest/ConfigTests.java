package applicationTest;


import application.dao.impls.*;
import application.dao.interfaces.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "applicationTest")
public class ConfigTests {
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:applicationTest.test;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=4");
//       dataSource.setUrl("jdbc:h2:tcp://localhost/~/test;DB_CLOSE_DELAY=-1;TRACE_LEVEL_FILE=4");
        return dataSource;
    }

    @Value("classpath:sql/schema.sql")
    private Resource schemaScript;

    @Value("classpath:sql/data.sql")
    private Resource dataScript;


    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }

    @Bean
    public Factory getFactory(){
        return new FactoryImpl();
    }

    @Bean
    public IngredientDAO getIngredientDAO() {
        return new IngredientDAOImpl(getDataSource());
    }

    @Bean
    public MeasureDAO getMeasureDAO() {
        return new MeasureDAOImpl(getDataSource());
    }

    @Bean
    public OrganizationDAO getOrganizationDAO() {
        return new OrganizationDAOImpl(getDataSource());
    }

    @Bean
    public MenuDAO getMenuDAO() {
        return new MenuDAOImpl(getDataSource());
    }

    @Bean
    public MenuSectionDAO getSectionItemDAO() {
        return new MenuSectionDAOImpl(getDataSource());
    }

    @Bean
    public MenuItemDAO getMenuItemDetailDAO() {
        return new MenuItemDAOImpl(getDataSource());
    }
}
