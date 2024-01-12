FROM azul/zulu-openjdk:17-latest
VOLUME /tmp
COPY build/libs/final_store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM eclipse-temurin:17-jdk-jammy AS build
#COPY . .
#RUN ./gradlew bootJar --no-daemon
#
#FROM openjdk:17.0.1-jdk-slim
#COPY --from=build /build/libs/final_store-1.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","demo.jar"]
