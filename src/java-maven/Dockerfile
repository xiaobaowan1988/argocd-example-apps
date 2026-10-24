FROM mirror.gcr.io/library/maven:3.9-eclipse-temurin-21 AS build
ARG VERSION=dev
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package && printf '%s' "$VERSION" > /src/version.txt

FROM mirror.gcr.io/library/eclipse-temurin:21-jre
COPY --from=build /src/target/java-api-1.0.jar /app/app.jar
COPY --from=build /src/version.txt /app/version.txt
USER 65532:65532
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
