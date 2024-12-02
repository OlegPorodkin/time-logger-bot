FROM maven:3.9.9-eclipse-temurin-21 as builder

WORKDIR /app

COPY . /app/

RUN ls -l /app && ls -l /app

RUN mvn clean install

RUN mvn dependency:resolve dependency:resolve-plugins

RUN mvn clean package spring-boot:repackage -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms512m -Xmx1024m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]