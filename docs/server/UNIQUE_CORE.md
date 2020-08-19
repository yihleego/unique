# Unique Core

## Dependency

To use the `unique-core` module, you need to include the `unique-core-0.1.0.jar` file and its dependencies in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Getting Started

### JDBC

Take MySQL as an example, you need to include the `mysql-connector-java-x.x.x.jar` file.

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```

Create a schema and table.

```mysql
create database `unique`;
use `unique`;
create table `sequence`
(
    `seq_key`         varchar(127) primary key     not null comment 'key',
    `seq_value`       bigint unsigned default 0    not null comment 'value',
    `seq_increment`   int unsigned    default 1    not null comment 'increment',
    `seq_cache`       int unsigned    default 5000 not null comment 'cache size',
    `seq_version`     int unsigned    default 1    not null comment 'version',
    `seq_create_time` timestamp                    null comment 'create time',
    `seq_update_time` timestamp                    null comment 'update time'
) engine = InnoDB
  default charset = utf8mb4
  collate utf8mb4_unicode_ci
    comment 'sequence';
```

Notice that the `JdbcSequenceManagerImpl` requires a `DataSource`. This can be any `DataSource` and should be configured.

```java
String driverClassName = "com.mysql.cj.jdbc.Driver";
String url = "jdbc:mysql://localhost:3306/unique?serverTimezone=GMT";
String username = "username";
String password = "password";
String table = "sequence";
HikariConfig config = new HikariConfig();
config.setDriverClassName(driverClassName);
config.setJdbcUrl(url);
config.setUsername(username);
config.setPassword(password);
DataSource dataSource = new HikariDataSource(config);
SequenceManager sequenceManager = new JdbcSequenceManagerImpl(dataSource, table);
```

Use standalone mode.

```java
SequenceService sequenceService = new StandaloneSequenceServiceImpl(sequenceManager);
```

Use cluster mode.

```java
SequenceService sequenceService = new ClusterSequenceServiceImpl(sequenceManager);
```

### MongoDB

You need to include the `mongodb-driver-sync-x.x.x.jar` file.

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>${mongodb.version}</version>
</dependency>
```

Notice that the `MongoSequenceManagerImpl` requires a `MongoClient`.

```java
String uri = "mongodb://localhost:27017";
String username = "username";
String password = "password";
String database = "unique";
String collection = "sequence";
MongoCredential credential = MongoCredential.createCredential(
        username,
        database,
        password.toCharArray());
MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString(uri))
        .credential(credential)
        .build();
MongoClient mongoClient = MongoClients.create(settings);
SequenceManager sequenceManager = new MongoSequenceManagerImpl(mongoClient, database, collection);
```

Use standalone mode.

```java
SequenceService sequenceService = new StandaloneSequenceServiceImpl(sequenceManager);
```

Use cluster mode.

```java
SequenceService sequenceService = new ClusterSequenceServiceImpl(sequenceManager);
```

### Use with client

Use the simple-client. Please use `UniqueClients.newCached` if the servers are clustered.

```java
UniqueClient client = UniqueClients.newSimple(sequenceService);
```

Use the cached-client.

```java
UniqueClient client = UniqueClients.newCached(sequenceService);
```
