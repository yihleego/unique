# Unique Server Spring Boot Starter

## Dependency

To use the `unique-server-spring-boot-starter` module, you need to include the `unique-server-spring-boot-starter-0.1.0.jar` file and its dependencies in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-server-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```
## Configuration

|Property|Description|
|:-|:-|
|spring.unique.jdbc.enabled|Whether to enable JDBC.|
|spring.unique.jdbc.driver-class-name|Fully qualified name of the JDBC driver.|
|spring.unique.jdbc.url|JDBC URL of the database.|
|spring.unique.jdbc.username|Login username of the database.|
|spring.unique.jdbc.password|Login password of the database.|
|spring.unique.jdbc.table|Table name.|
|spring.unique.mongodb.enabled|Whether to enable MongoDB.|
|spring.unique.mongodb.uri|Mongo database URI.|
|spring.unique.mongodb.username|Login user of the mongo server.|
|spring.unique.mongodb.password|Login password of the mongo server.|
|spring.unique.mongodb.database|Database name.|
|spring.unique.mongodb.collection|Collection name.|
|spring.unique.cluster.enabled|Whether to enable cluster.|
|spring.unique.cluster.retries|Retries|
|spring.unique.cluster.allow-single|Whether to allow obtaining single sequence.|
|spring.unique.console.enabled|Whether to enable console.|

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

Properties

```properties
spring.unique.jdbc.enabled=true
spring.unique.jdbc.driver-class-name=com.mysql.cj.jdbc.Driver
spring.unique.jdbc.url=jdbc:mysql://localhost:3306/unique?serverTimezone=GMT
spring.unique.jdbc.username=username
spring.unique.jdbc.password=password
spring.unique.jdbc.table=sequence
```

Yaml

```yaml
spring:
  unique:
    jdbc:
      enabled: true
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/unique?serverTimezone=GMT
      username: username
      password: password
      table: sequence
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

Properties

```properties
spring.unique.mongodb.enabled=true
spring.unique.mongodb.uri=mongodb://localhost:27017
spring.unique.mongodb.username=username
spring.unique.mongodb.password=password
spring.unique.mongodb.database=unique
spring.unique.mongodb.collection=sequence
```

Yaml

```yaml
spring:
  unique:
    mongodb:
      enabled: false
      uri: mongodb://localhost:27017
      username: username
      password: password
      database: unique
      collection: sequence
```
### Usage

Obtain one sequence named `test-key`.

```bash
curl --location --request GET 'https://localhost:8080/sequences/test-key' --header 'Content-Type: application/json'
```

Obtain several sequences named `test-key`.

```bash
curl --location --request GET 'https://localhost:8080/sequences/test-key/segments?size=10' --header 'Content-Type: application/json'
```

## Enable Cluster

Properties

```properties
spring.unique.cluster.enabled=true
spring.unique.cluster.retries=10
```

Yaml

```yaml
spring:
  unique:
    cluster:
      enabled: true
      retries: 10
```

## Enable Console

See the [unique-server-console](UNIQUE_SERVER_CONSOLE.md) for details.

You need to include the `unique-server-console-0.1.0.jar` file.

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-server-console</artifactId>
    <version>0.1.0</version>
</dependency>
```

Properties

```properties
spring.unique.console.enabled=true
```

Yaml

```yaml
spring:
  unique:
    console:
      enabled: true
```