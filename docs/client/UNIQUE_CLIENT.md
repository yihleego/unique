# Unique Client

## Dependency

To use the `unique-client` module, you need to include the `unique-client-0.1.0.jar` file and its dependencies in the classpath.

If you are using Maven just add the following dependency to your pom.xml:

```xml
<dependency>
    <groupId>io.leego</groupId>
    <artifactId>unique-client</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Getting Started

There are two kinds of clients: `SimpleUniqueClient` and `CachedUniqueClient`.

**Notice that the `SimpleUniqueClient`'s performance is terrible in cluster mode, please use `CachedUniqueClient` instead of `SimpleUniqueClient`** 

Assume there is an instance of the UniqueService named uniqueService.

```java
UniqueClient simple = new SimpleUniqueClient(uniqueService);
UniqueClient cached = new CachedUniqueClient(uniqueService);
```

## Factory

Create an instance of `SimpleUniqueClient`.

```java
UniqueClient client = UniqueClients.newSimple(uniqueService);
```

Create an instance of `CachedUniqueClient`.

```java
Integer cacheSize = 100;
Duration timeout = Duration.ofSeconds(3L);
UniqueClient client = UniqueClients.newCached(uniqueService, cacheSize, timeout);
```

## Builder

Set with `URL`.

```java
UniqueClient client = UniqueClients.builder()
    .url("https://localhost:8080")
    .timeout(Duration.ofSeconds(3L))
    .cached()
    .cacheSize(100)
    .build();
```

Set with `host`, `port` and `SSL`.

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

## Usage

Obtain one sequence named `test-key`.

```java
// It is equivalent to the cURL command line: 
// curl --location --request GET 'https://localhost:8080/sequences/test-key' --header 'Content-Type: application/json'
long value = client.next("test-key");
// Outputs result
System.out.println(value);
```

Obtain several sequences named `test-key`.

```java
// It is equivalent to the cURL command line: 
// curl --location --request GET 'https://localhost:8080/sequences/test-key/segments?size=10' --header 'Content-Type: application/json'
LinkedList<Long> values = client.next("test-key", 10);
// Outputs result
while (!values.isEmpty()) {
    System.out.println(values.remove());
}
```


