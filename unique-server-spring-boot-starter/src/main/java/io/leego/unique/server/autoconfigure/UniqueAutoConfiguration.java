package io.leego.unique.server.autoconfigure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.leego.unique.common.util.DatePattern;
import io.leego.unique.common.util.StringUtils;
import io.leego.unique.core.manager.SequenceManager;
import io.leego.unique.core.manager.impl.JdbcSequenceManagerImpl;
import io.leego.unique.core.manager.impl.MongoSequenceManagerImpl;
import io.leego.unique.core.service.SequenceService;
import io.leego.unique.core.service.impl.SequenceServiceImpl;
import io.leego.unique.server.console.service.ConsoleService;
import io.leego.unique.server.console.service.impl.ConsoleServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Yihleego
 */
@Configuration
@ComponentScan(basePackages = "io.leego.unique.server")
@EnableConfigurationProperties(UniqueServerProperties.class)
public class UniqueAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.DATE_TIME);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DatePattern.DATE);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DatePattern.TIME);
        SimpleModule javaTimeModule = new JavaTimeModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        return new ObjectMapper()
                .registerModules(new ParameterNamesModule(), new Jdk8Module(), javaTimeModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @Bean
    @ConditionalOnBean(SequenceManager.class)
    @ConditionalOnMissingBean(SequenceService.class)
    public SequenceService sequenceService(SequenceManager sequenceManager) {
        SequenceService sequenceService = new SequenceServiceImpl(sequenceManager);
        sequenceService.init();
        return sequenceService;
    }

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
            if (StringUtils.isNotEmpty(username)) {
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

    }

    @Configuration
    @ConditionalOnClass(ConsoleService.class)
    @AutoConfigureAfter({UniqueJdbcAutoConfiguration.class, UniqueMongoAutoConfiguration.class})
    protected static class UniqueConsoleAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean(ConsoleService.class)
        public ConsoleService consoleService(SequenceManager sequenceManager, SequenceService sequenceService) {
            return new ConsoleServiceImpl(sequenceManager, sequenceService);
        }

    }

}