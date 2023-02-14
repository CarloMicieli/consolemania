# Catalog Service

## REST API

|        Endpoint	        |  Method  | Req. body  | Status |  Resp. body  | Description    		                                    |
|:-----------------------:|:--------:|:----------:|:------:|:------------:|:-----------------------------------------------------|
|        `/games`         |  `POST`  |   `Game`   |  201   |              | Add a new game to the catalog                        |
|      `/games/{id}`      |  `PUT`   |   `Game`   |  204   |              | Update the game with the given `{id}`                |
|      `/games/{id}`      |  `GET`   |            |  200   |    `Game`    | Get the game with the given `{id}`                   |
|      `/games/{id}`      | `DELETE` |            |  204   |              | Delete the game with the given `{id}`                |
|      `/platforms`       |  `POST`  | `Platform` |  201   |              | Add a new platform to the catalog                    |
|    `/platforms/{id}`    |  `PUT`   | `Platform` |  204   |              | Update the platform with the given `{id}`            |
|      `/platforms`       |  `GET`   |            |  200   | `Platform[]` | Get all platforms in the catalog                     |
|    `/platforms/{id}`    |  `GET`   |            |  200   |  `Platform`  | Get the platform with the given `{id}`               |
| `/platforms/{id}/games` |  `GET`   |            |  200   |   `Game[]`   | Get all games for the platform with the given `{id}` |

## Useful Commands

| Gradle Command	            | Description                                   |
|:---------------------------|:----------------------------------------------|
| `./gradlew bootRun`        | Run the application.                          |
| `./gradlew build`          | Build the application.                        |
| `./gradlew test`           | Run tests.                                    |
| `./gradlew bootJar`        | Package the application as a JAR.             |
| `./gradlew bootBuildImage` | Package the application as a container image. |

After building the application, you can also run it from the Java CLI:

```bash
java -jar build/libs/catalog-service-0.0.0-SNAPSHOT.jar
```

## Running a PostgreSQL Database

Run PostgreSQL as a Docker container

```bash
docker run  -it --rm --name catalog-db-postgres \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=mysecretpassword \
    -e POSTGRES_DB=catalogdb \
    -p 5432:5432 \
    postgres:15.1-alpine
```
