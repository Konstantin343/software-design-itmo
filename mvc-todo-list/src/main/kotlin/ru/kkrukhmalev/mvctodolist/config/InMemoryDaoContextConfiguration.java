package ru.kkrukhmalev.mvctodolist.config;

import org.springframework.context.annotation.Bean;
import ru.kkrukhmalev.mvctodolist.dao.TodoDao;
import ru.kkrukhmalev.mvctodolist.dao.TodoInMemoryDao;

/**
 * @author akirakozov
 */
public class InMemoryDaoContextConfiguration {
    @Bean
    public TodoDao productDao() {
        return new TodoInMemoryDao();
    }
}
