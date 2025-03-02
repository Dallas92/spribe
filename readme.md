# Spribe app

API that allows the following operations:
* Create units
* Search units
* Get stats of available unit ids
* Book units
* Cancel booking
* Make payment
* Auto-cancellation for bookings that older than 15 minutes

## Requirements

- Java 21 or higher

## Technologies
- Java 21
- Spring Boot 3.x.x
- Gradle
- Postgres
- Hibernate
- Redis
- MapStruct
- Swagger
- Liquibase
- Lombok


## API documentation

### Swagger plugin
```bash
http://localhost:8080/swagger-ui/index.html
```

## How to run the Application

To run the application do the following:

1. Run docker-compose with command

```bash
docker-compose up -d 
```

2. Start application
3. Send some requests via Swagger UI
