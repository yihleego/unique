# Unique Core

## 添加依赖

使用`unique-core`模块, 需要在类路径中包含`unique-core-0.1.0.jar`文件及其依赖项。

Maven项目中只需将以下依赖项添加到pom.xml文件:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 开始使用

### JDBC

以MySQL为例，需要在类路径中包含`MySQL-connector-java-x.x.x.jar`文件。

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
</dependency>
```

创建数据库和表。

[点击查看更多数据库](../../resources/jdbc)

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

实例化`JdbcSequenceManagerImpl`需要一个`DataSource`对象。

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

使用单机模式。

```java
SequenceService sequenceService = new StandaloneSequenceServiceImpl(sequenceManager);
```

使用群集模式。

```java
SequenceService sequenceService = new ClusterSequenceServiceImpl(sequenceManager);
```

### MongoDB

需要在类路径中包含`mongodb-driver-sync-x.x.x.jar`文件。

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>${mongodb.version}</version>
</dependency>
```

实例化`MongoSequenceManagerImpl`需要一个`MongoClient`对象。

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

使用单机模式。

```java
SequenceService sequenceService = new StandaloneSequenceServiceImpl(sequenceManager);
```

使用群集模式。

```java
SequenceService sequenceService = new ClusterSequenceServiceImpl(sequenceManager);
```

### 与客户端一起使用

有关客户端详细信息，请参见[unique-client](../client/UNIQUE_CLIENT.ZH_CN.md)。

**请注意集群模式下`SimpleUniqueClient`性能非常糟糕, 请使用`CachedUniqueClient`。** 

```java
// SimpleUniqueClient
UniqueClient simpleClient = UniqueClients.newSimple(sequenceService);
// CachedUniqueClient
UniqueClient cachedClient = UniqueClients.newCached(sequenceService);
```
