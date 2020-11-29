
## Requirements

* JDK 11
* Docker & Docker Compose

## Configuration

To run the project you need to specify credentials in application.properties
```
cp bot/src/main/resources/application.properties /path/to/your/application.properties
```

## Building
```
mvn clean install
```
## Deployment
```
docker-compose build
docker-compose up
```