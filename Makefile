.PHONY: build

ifeq ($(OS),Windows_NT)
    GRADLEW := gradlew.bat
    DOCKER_COMPOSE := docker compose
else
    GRADLEW := ./gradlew
    DOCKER_COMPOSE := docker-compose
endif

build:
	$(GRADLEW) clean build

up:
	$(DOCKER_COMPOSE) up -d --build

down:
	$(DOCKER_COMPOSE) stop

restart: down up