# Unique Server Spring Boot Starter

## 添加依赖

使用`unique-server-spring-boot-starter`模块, 需要在类路径中包含`unique-server-spring-boot-starter-0.1.0.jar`文件及其依赖项。

Maven项目中只需将以下依赖项添加到pom.xml文件:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-server-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```
## 配置

|Property|Description|
|:-|:-|
|spring.unique.jdbc.enabled|是否启用JDBC。|
|spring.unique.jdbc.driver-class-name|JDBC驱动类名。|
|spring.unique.jdbc.url|数据库的JDBC URL。|
|spring.unique.jdbc.username|数据库的登录用户名。|
|spring.unique.jdbc.password|数据库的登录密码。|
|spring.unique.jdbc.table|表名。|
|spring.unique.mongodb.enabled|是否启用MongoDB。|
|spring.unique.mongodb.uri|MongoDB数据库URI。|
|spring.unique.mongodb.username|MongoDB服务器的登录用户。|
|spring.unique.mongodb.password|MongoDB服务器的登录密码。|
|spring.unique.mongodb.database|数据库名称。|
|spring.unique.mongodb.collection|集合名称。|
|spring.unique.cluster.enabled|是否启用群集。|
|spring.unique.cluster.retries|重试次数。|
|spring.unique.cluster.allow-single|集群模式下是否允许获取单个序列。|
|spring.unique.console.enabled|是否启用控制台。|

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
### 使用方法

#### 使用客户端

推荐使用客户端，有关客户端详细信息，请参见[unique-client](UNIQUE_CLIENT.ZH_CN.md)。

#### 使用cURL

获取一个名称为`test-key`的序列号。

```bash
curl --location --request GET 'https://localhost:8080/sequences/test-key' --header 'Content-Type: application/json'
```

获取多个名称为`test-key`的序列号。

```bash
curl --location --request GET 'https://localhost:8080/sequences/test-key/segments?size=10' --header 'Content-Type: application/json'
```

## 启用群集
   
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

## 启用控制台

有关控制台详细信息，请参见[unique-server-console](UNIQUE_SERVER_CONSOLE.ZH_CN.md)。

需要在类路径中包含`unique-server-console-0.1.0.jar`文件.

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