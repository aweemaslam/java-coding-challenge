package com.crewmeister.cmcodingchallenge.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configures distributed locking using ShedLock with JDBC.
 */
@Configuration
public class ShedLockConfig {

    /**
     * Provides a JDBC-based lock provider using the application's DataSource.
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}