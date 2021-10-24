package ru.kkrukhmalev.mvctodolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.kkrukhmalev.mvctodolist.dao.TodoDao;
import ru.kkrukhmalev.mvctodolist.dao.TodoJdbcDao;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @author akirakozov
 */
@Configuration
public class JdbcDaoContextConfiguration {
    @Bean
    public TodoDao productJdbcDao(DataSource dataSource) {
        return new TodoJdbcDao(dataSource);
    }

    @Bean
    public DataSource dataSource() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("database.properties"));
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.getProperty("database.driver"));
        dataSource.setUrl(properties.getProperty("database.url"));
        dataSource.setUsername(properties.getProperty("database.user"));
        dataSource.setPassword(properties.getProperty("database.password"));
        return dataSource;
    }
}
