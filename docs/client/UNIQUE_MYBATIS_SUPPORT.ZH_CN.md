# Unique MyBatis Support

## 添加依赖

使用`unique-mybatis-support`模块, 需要在类路径中包含`unique-mybatis-support-0.1.0.jar`文件及其依赖项。

Maven项目中只需将以下依赖项添加到pom.xml文件:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-mybatis-support</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 配置

### MyBatis SqlSessionFactoryBean

假设存在一个变量名为`uniqueClient`的`UniqueClient`类的实例。

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

## 使用方法

假设存在如下定义的类：

```java
import io.leego.unique.mybatis.support.annotation.AutoSeq;

public class Foo {
    // 假设对应序列的名称是"foo"
    @AutoSeq("foo")
    private Long id;
    private String name;
    /* getter setter */
}
```

假设存在如下定义的Mapper：

```java
public interface FooMapper {
    @Select("INSERT INTO foo VALUES(#{id}, #{name})")
    int insert(Foo foo);
}
```

调用保存接口：

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