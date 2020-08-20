# Unique Client

## 添加依赖

使用`unique-client`模块, 需要在类路径中包含`unique-client-0.1.0.jar`文件及其依赖项。

Maven项目中只需将以下依赖项添加到pom.xml文件:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-client</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 开始使用

现有两种客户端可供使用: `SimpleUniqueClient` and `CachedUniqueClient`。

**请注意集群模式下`SimpleUniqueClient`性能非常糟糕, 请使用`CachedUniqueClient`。** 

假设存在一个变量名为`uniqueService`的`UniqueService`类的实例。

```java
UniqueClient simpleClient = new SimpleUniqueClient(uniqueService);
UniqueClient cachedClient = new CachedUniqueClient(uniqueService);
```

## 工厂模式

使用`UniqueClients`.

```java
// SimpleUniqueClient
UniqueClient simpleClient = UniqueClients.newSimple(uniqueService);
// CachedUniqueClient
Integer cacheSize = 100;
Duration timeout = Duration.ofSeconds(3L);
UniqueClient cachedClient = UniqueClients.newCached(uniqueService, cacheSize, timeout);
```

## 建造者模式

设置`URL`。

```java
UniqueClient client = UniqueClients.builder()
    .url("https://localhost:8080")
    .timeout(Duration.ofSeconds(3L))
    .cached()
    .cacheSize(100)
    .build();
```

设置`host`、`port`和`SSL`。

```java
UniqueClient client = UniqueClients.builder()
    .host("localhost")
    .port(8080)
    .ssl()
    .timeout(Duration.ofSeconds(3L))
    .cached()
    .cacheSize(100)
    .build();
```

## 使用方法

获取一个名称为`test-key`的序列号。

```java
// 相当于cURL命令行：
// curl --location --request GET 'https://localhost:8080/sequences/test-key' --header 'Content-Type: application/json'
long value = client.next("test-key");
// 输出结果
System.out.println(value);
```

获取多个名称为`test-key`的序列号。

```java
// 相当于cURL命令行：
// curl --location --request GET 'https://localhost:8080/sequences/test-key/segments?size=10' --header 'Content-Type: application/json'
LinkedList<Long> values = client.next("test-key", 10);
// 输出结果
while (!values.isEmpty()) {
    System.out.println(values.remove());
}
```


