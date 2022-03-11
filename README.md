# Buildings API Assignment

## Introduction

This project is intended to test your Java skills with a focus on building web APIs. Refer to the email sent to you on
how to complete this assignment. Feel free to use, modify, or delete any/all of the provided skeleton code as desired.

## Getting to know your project

### Docker

As a shortcut, to avoid any issues with installing Java or Maven locally, you can build/test/run the app with Docker:

    docker build -t buildings-api-assignment .
    docker run -p8080:8080 buildings-api-assignment

### Java

This project uses Java 8+. To check your installed version, run `javac --version` - you'll need version 8 or higher.

There are numerous platform-specific ways to install Java, but some options:

* [Official JDK download](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Homebrew](https://devqa.io/brew-install-java/)
* [SDKMAN!](https://sdkman.io/)

### Maven

This is project is built using [Maven](https://maven.apache.org/index.html). Refer to
the [official installation instructions](https://maven.apache.org/install.html), or try one of these quickstart methods
for Mac OS and Ubuntu.

If you are running on a
Mac: [Install Maven on a Mac](https://www.code2bits.com/how-to-install-maven-on-macos-using-homebrew/)

```bash
brew update
brew search maven
brew info maven
brew install maven
brew cleanup
mvn -version
```

If you are running on Ubuntu:

```bash
sudo apt update
sudo apt install maven
mvn -version
```

### How to build and run the project

1. From command line, go to the root of the project
2. `mvn clean install` - this also runs all the unit tests
3. `mvn spring-boot:run`

- this invokes the project and binds the server to port 8080

### How to invoke and access the default (root) controller

1. From the command line `curl localhost:8080`
2. From a browser http://localhost:8080
3. From Postman `GET http://localhost:8080`

### Database

To simplify data access, this project uses an embedded H2 database that is automatically started with the app.

* Console URL: http://localhost:8080/h2-console
* Username: measurabl
* Password: measurabl
* JDBC URL: jdbc:h2:mem:measurabl

### Special notes

1. This project uses [Lombok](https://projectlombok.org) to generate boilerplate code
2. We strongly recommend [IntelliJ](https://www.jetbrains.com/idea/download/) - the community edition is free
3. As a reminder, feel free to use, modify, or delete any/all of the provided skeleton code

### Swagger enabled

This project has Swagger enabled; all endpoints in this project are automatically documented.

* HTML formatted documentation -> http://localhost:8080/swagger-ui.html#/
* Machine readable API http://localhost:8080/v2/api-docs 
