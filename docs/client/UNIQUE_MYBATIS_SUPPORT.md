# Unique MyBatis Support

## Dependency

To use the `unique-mybatis-support` module, you need to include the `unique-mybatis-support-0.1.0.jar` file and its dependencies in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-mybatis-support</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Configuration

### MyBatis SqlSessionFactoryBean

Assume there is an instance of the `UniqueClient` named `uniqueClient`.

```java
AutoSeqInterceptor interceptor = new AutoSeqInterceptor(uniqueClient);
Interceptor[] plugins = new Interceptor[]{interceptor};
SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
sqlSessionFactoryBean.setPlugins(plugins);
```

### Spring Boot

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

## Usage

Assume there is a class defined like the following:

```java
import io.leego.unique.mybatis.support.annotation.AutoSeq;

public class Foo {
    // Assume the name of the sequence is "foo".
    @AutoSeq("foo")
    private Long id;
    private String name;
    /* getter setter */
}
```

Assume there is a mapper interface defined like the following:

```java
public interface FooMapper {
    @Select("INSERT INTO foo VALUES(#{id}, #{name})")
    int insert(Foo foo);
}
```

Call insert method:

```java
@Service
public class Tests {
    @Autowired
    private FooMapper fooMapper;

    public int insert() {
        Foo foo = new Foo();
        foo.setName("foo");
        return fooMapper.insert(foo);
    }
}
```