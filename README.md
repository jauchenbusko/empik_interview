# Getting Started

## Overview

Simple rest service for getting User Information from github API.
Provides REST method `GET /users/{login}` which returns user information for user with `{login}`:

```json
{
  "id": "...",
  "login": "...",
  "name": "...",
  "type": "...",
  "avatarUrl":"...",
  "createdAt": "...",
  "calculations": "..."
}
```

Calculations field refers to `6.0 / followers * 2 + publicRepos` of user with `{login}`

Collects number od interactions with user `{login}` in DB

### Build

In order to build the app execute:

```bash
mvn clean install
```

In order to run build with docker image build execute:

```bash
mvn clean spring-boot:build-image
```

### Run User Data App

## Using command line

Application requires MySql database installed and properties of DB connections must be set up:

`spring.datasource.url=jdbc`
`spring.datasource.username`
`spring.datasource.password`

In order to run Application execute

```bash
java -jar interview-0.0.1-SNAPSHOT.jar
```

## Using docker

Application requires Docker installed

This project contains a Docker Compose file named `docker-compose-all.yaml` in projectDir/docker
In this file, the following services have been defined:

* database: [`mysql:latest`](https://hub.docker.com/_/mysql)
* spring-app: `interview:0.0.1-SNAPSHOT`

Please review the tags of the used images and set them to the same as you're running service.

In order to run Application with docker compose execute

```bash
docker compose up
```

## Run application with database with simple test

App could be started with `src/test/java/com/yb/empik/task/interview/TestInterviewApplication.java`
This option will start db container in docker and run application.
Option includes running of simple test calling the API.

