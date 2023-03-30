# bowling

This project uses Quarkus, the Supersonic Subatomic Java Framework.

It includes a set of rest API to
- Create a bowling game
- Add players
- Register ball rolls
- Run a simulation of a 10 fram game set with a provided number of players

Please refere to swagger-ui to know more about service endpoints.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```
> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.

To build a bowling-app container run:
```shell script
./gradlew build -Dquarkus.container-image.build=true    
```
This script should provide you a "user/bowling docker" image
Run docker image and accces swagger-ui page to inspect the avaliable endpoints.

```shell script
docker run -p 8080:8080 user/bowling   
```
Swagger-ui page url: `http://localhost:8080/q/swagger-ui/`