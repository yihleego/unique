# Unique Client Spring Boot Starter

## Dependency

To use the `unique-client-spring-boot-starter` module, you need to include the `unique-client-spring-boot-starter-0.1.0.jar` file and its dependencies in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-client-spring-boot-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```
## Configuration

|Property|Description|
|:-|:-|
|spring.unique.enabled|Whether to enable auto-configure.|
|spring.unique.uri|The absolute URL or resolvable hostname (the protocol is optional).|
|spring.unique.service-id|The name of the service with optional protocol prefix.|
|spring.unique.timeout|Request timeout.|
|spring.unique.cache.enabled|Whether to enable caching|
|spring.unique.cache.size|Cache size.|
|spring.unique.hystrix.enabled|Whether to enable Hystrix.|
|spring.unique.mybatis.plugin.enabled|Whether to enable mybatis-plugin.|
|spring.unique.validation.enabled|Whether to enable validation.|
|spring.unique.validation.keys|Keys to be validated.|

## Getting Started

### HTTP

Assume there is a sequence service running {host: localhost, port: 8080}.

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

Assume there is a sequence service named `sequence` is registered.

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

See the [unique-client](UNIQUE_CLIENT.md) for details.

Obtain one sequence named `test-key`.

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

## Enable Mybatis Support

See the [unique-mybatis-support](UNIQUE_MYBATIS_SUPPORT.md) for details.

You need to include the `unique-mybatis-support-0.1.0.jar` file.

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