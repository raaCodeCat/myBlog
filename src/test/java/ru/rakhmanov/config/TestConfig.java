package ru.rakhmanov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import ru.rakhmanov.repository.impl.JdbcCommentRepository;
import ru.rakhmanov.repository.impl.JdbcLikeRepository;
import ru.rakhmanov.repository.impl.JdbcPostRepository;
import ru.rakhmanov.repository.impl.JdbcTagRepository;

import javax.sql.DataSource;

@Configuration
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @EventListener
    public void populate(ContextRefreshedEvent event) {
        DataSource dataSource = event.getApplicationContext().getBean(DataSource.class);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);
    }

    @Bean
    public JdbcCommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcCommentRepository(jdbcTemplate);
    }

    @Bean
    public JdbcLikeRepository likeRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcLikeRepository(jdbcTemplate);
    }

    @Bean
    public JdbcPostRepository postRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcPostRepository(jdbcTemplate);
    }

    @Bean
    public JdbcTagRepository tagRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcTagRepository(jdbcTemplate);
    }
}