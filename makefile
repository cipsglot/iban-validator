.PHONY: compile

compile:
	./gradlew clean build

build-jar: compile
	./gradlew uberJar

docker-build: build-jar
	docker build -t iban-validator-service -f src/deploy/Dockerfile .

docker-start:
	docker-compose up
