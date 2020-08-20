# Unique Client Spring Boot Starter

## 添加依赖

使用`unique-client-spring-boot-starter`模块, 需要在类路径中包含`unique-client-spring-boot-starter-0.1.0.jar`文件及其依赖项。

Maven项目中只需将以下依赖项添加到pom.xml文件:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-client-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```
## 配置

|Property|Description|
|:-|:-|
|spring.unique.enabled|是否启用自动配置。|
|spring.unique.uri|URL或可解析主机名。|
|spring.unique.service-id|具有可选协议前缀的服务的名称。|
|spring.unique.timeout|请求超时时间。|
|spring.unique.cache.enabled|是否启用缓存。|
|spring.unique.cache.size|缓存大小。|
|spring.unique.hystrix.enabled|是否启用`Hystrix`。|
|spring.unique.mybatis.plugin.enabled|是否启用`MyBatis`插件。|
|spring.unique.validation.enabled|是否启用校验。|
|spring.unique.validation.keys|要校验的Keys。|

## 开始使用

### HTTP

假设有一个sequence服务运行{主机名: localhost, 端口: 8080}。

Properties

```properties
spring.unique.enabled=true
spring.unique.uri=https://localhost:8080
```

Yaml

```yaml
spring:
  unique:
    enabled: true
    uri: https://localhost:8080
```

### Spring Cloud

假设有一个`service-id`为`sequence`的服务已在注册中心注册成功。

Properties

```properties
spring.unique.enabled=true
spring.unique.service-id=sequence
```

Yaml

```yaml
spring:
  unique:
    enabled: true
    service-id: sequence
```

## Usage

有关客户端详细信息，请参见[unique-client](UNIQUE_CLIENT.ZH_CN.md)。

获取一个名称为`test-key`的序列号。

```java
@Service
public class Tests {
    @Autowired
    private UniqueClient client;

    public void test() {
        long value = client.next("test-key");
        System.out.println(value);
    }
}
```

## 启用MyBatis支持

有关MyBatis支持详细信息，请参见[unique-mybatis-support](UNIQUE_MYBATIS_SUPPORT.ZH_CN.md)。

需要在类路径中包含`unique-mybatis-support-0.1.0.jar`文件。

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-mybatis-support</artifactId>
    <version>0.1.0</version>
</dependency>
```

Properties

```properties
spring.unique.mybatis.plugin.enabled=true
```

Yaml

```yaml
spring:
  unique:
    mybatis:
      plugin:
        enabled: true
```