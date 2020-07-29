package io.leego.unique.core.manager.impl;

import io.leego.unique.common.exception.InvalidTableException;
import io.leego.unique.core.dao.SequenceDAO;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.manager.SequenceManager;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.JdbcType;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yihleego
 */
public class JdbcSequenceManagerImpl implements SequenceManager {
    protected final DataSource dataSource;
    protected final SqlSessionFactory sqlSessionFactory;
    protected final String tableName;
    protected final Pattern tableNamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");

    public JdbcSequenceManagerImpl(DataSource dataSource, String tableName) {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(tableName);
        this.validateTableName(tableName);
        this.dataSource = dataSource;
        this.sqlSessionFactory = newSqlSessionFactory(dataSource);
        this.tableName = tableName;
    }

    @Override
    public List<Sequence> query() {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            SequenceDAO dao = session.getMapper(SequenceDAO.class);
            return dao.query(tableName);
        }
    }

    @Override
    public int updateValue(String key, long value) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            SequenceDAO dao = session.getMapper(SequenceDAO.class);
            return dao.updateValue(key, value, tableName);
        }
    }

    @Override
    public int save(Sequence sequence) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            SequenceDAO dao = session.getMapper(SequenceDAO.class);
            return dao.save(sequence, tableName);
        }
    }

    @Override
    public int update(Sequence sequence) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            SequenceDAO dao = session.getMapper(SequenceDAO.class);
            return dao.update(sequence, tableName);
        }
    }

    @Override
    public int delete(String key) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            SequenceDAO dao = session.getMapper(SequenceDAO.class);
            return dao.delete(key, tableName);
        }
    }

    protected SqlSessionFactory newSqlSessionFactory(DataSource dataSource) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment(SqlSessionFactory.class.getSimpleName(), transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setCacheEnabled(false);
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        configuration.setLazyLoadingEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.addMapper(SequenceDAO.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    protected void validateTableName(String tableName) {
        Matcher matcher = tableNamePattern.matcher(tableName);
        if (!matcher.matches()) {
            throw new InvalidTableException("Invalid table name \"" + tableName + '\"');
        }
    }

}
