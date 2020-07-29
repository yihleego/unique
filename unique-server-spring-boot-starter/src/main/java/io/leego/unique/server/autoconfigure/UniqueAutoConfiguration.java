package io.leego.unique.server.autoconfigure;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.leego.unique.core.manager.SequenceManager;
import io.leego.unique.core.manager.impl.JdbcSequenceManagerImpl;
import io.leego.unique.core.manager.impl.MongoSequenceManagerImpl;
import io.leego.unique.core.service.SequenceService;
import io.leego.unique.core.service.impl.SequenceServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Yihleego
 */
@Configuration
@ComponentScan(basePackages = "io.leego.unique.server")
@ConditionalOnMissingBean(SequenceService.class)
@EnableConfigurationProperties(UniqueServerProperties.class)
public class UniqueAutoConfiguration {

    @Configuration
    @ConditionalOnProperty("spring.unique.jdbc.enabled")
    @EnableConfigurationProperties(UniqueServerProperties.class)
    protected static class UniqueJdbcAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(DataSource.class)
        public DataSource dataSource(UniqueServerProperties properties) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(properties.getJdbc().getDriverClassName());
            config.setJdbcUrl(properties.getJdbc().getUrl());
            config.setUsername(properties.getJdbc().getUsername());
            config.setPassword(properties.getJdbc().getPassword());
            return new HikariDataSource(config);
        }

        @Bean
        @ConditionalOnBean(DataSource.class)
        @ConditionalOnMissingBean(SequenceManager.class)
        public SequenceManager jdbcSequenceManager(DataSource dataSource, UniqueServerProperties properties) {
            return new JdbcSequenceManagerImpl(
                    dataSource,
                    properties.getJdbc().getTable());
        }

        @Bean
        @ConditionalOnBean(SequenceManager.class)
        public SequenceService sequenceService(SequenceManager sequenceManager) {
            SequenceService sequenceService = new SequenceServiceImpl(sequenceManager);
            sequenceService.load();
            return sequenceService;
        }

    }

    @Configuration
    @ConditionalOnClass(MongoClient.class)
    @ConditionalOnProperty("spring.unique.mongodb.enabled")
    @EnableConfigurationProperties(UniqueServerProperties.class)
    protected static class UniqueMongoAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(MongoClient.class)
        public MongoClient mongoClient(UniqueServerProperties properties) {
            String uri = properties.getMongodb().getUri();
            String username = properties.getMongodb().getUsername();
            String password = properties.getMongodb().getPassword();
            String database = properties.getMongodb().getDatabase();
            MongoClientSettings.Builder builder = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri));
            if (username != null && username.length() > 0) {
                MongoCredential credential = MongoCredential.createCredential(
                        username,
                        database,
                        password != null ? password.toCharArray() : null);
                builder.credential(credential);
            }
            return MongoClients.create(builder.build());
        }

        @Bean
        @ConditionalOnBean(MongoClient.class)
        @ConditionalOnMissingBean(SequenceManager.class)
        public SequenceManager mongoSequenceManager(MongoClient mongoClient, UniqueServerProperties properties) {
            return new MongoSequenceManagerImpl(
                    mongoClient,
                    properties.getMongodb().getDatabase(),
                    properties.getMongodb().getCollection());
        }

        @Bean
        @ConditionalOnBean(SequenceManager.class)
        public SequenceService sequenceService(SequenceManager sequenceManager) {
            SequenceService sequenceService = new SequenceServiceImpl(sequenceManager);
            sequenceService.load();
            return sequenceService;
        }

    }

}