FROM openjdk:11

EXPOSE 8091

RUN mkdir /app

ADD build/libs/*-uber.jar /app/iban-validator.jar

ENTRYPOINT ["java","-jar","/app/iban-validator.jar"]

