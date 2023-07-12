FROM eclipse-temurin:17-jdk-alpine

WORKDIR /controlepedagogico
COPY . .
RUN ./mvnw clean install
RUN ./mvnw test

CMD ./mvnw spring-boot:run