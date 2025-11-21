# Springboot Java: GeoCode Search
This is a geocode search application that uses Redis rather than a database to offer faster results. The GeoCode entity class has 3 attributes, as described in the data section. The _country_ and _city_ attributes uniquely identify each geocode. In Redis, you will make a _country_city_ storage key.

## Environment
- Java version: 17
- Maven version: 3.*
- Spring Boot version: 3.2.2
- Redis Version: 7.2.4

## Read-Only Files
- src/test/*
- setup/redisSetup.sh

## Commands
- run:
```bash
mvn clean spring-boot:run
```
- install:
```bash
bash setup/redisSetup.sh && mvn clean install
```
- test:
```bash
mvn clean test
```

## Sample Data

Here is an example of a GeoCode data JSON object:

```json
{
    "country": "United States",
    "city": "Boston",
    "code": "444557"
}
```

## Requirements

The project's REST endpoints are already implemented. Implement the Spring Boot Redis integration-related methods and class listed below.

`RedisConfig`
- Redis is available locally at the host and port in the _application.yml_ file.
- Implement the _JedisConnectionFactory_ and _RedisTemplate_ methods in the _RedisConfig_ class.

`GeoCodeRepository`
- Implement the methods for the operations _save_, _findByCountryAndCity_, and _findAll_, as mentioned in that class.
