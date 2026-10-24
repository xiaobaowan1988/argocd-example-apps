FROM mirror.gcr.io/library/maven:3.9-eclipse-temurin-21 AS build
ARG VERSION=dev
WORKDIR /src
# 可选:额外的 CA 证书(PEM)。出口 HTTPS 被中间人(企业代理、自签 CA 的内部 Nexus)时,Maven 的 JDK 信任库不认它,
# 会报 "PKIX path building failed";通过 builder 的 build_args 传入 EXTRA_CA_PEM 即可。为空时什么都不做。
ARG EXTRA_CA_PEM=""
RUN if [ -n "$EXTRA_CA_PEM" ]; then printf '%s\n' "$EXTRA_CA_PEM" > /tmp/extra-ca.crt \
 && keytool -importcert -noprompt -cacerts -storepass changeit -alias extra-ca -file /tmp/extra-ca.crt; fi
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package && printf '%s' "$VERSION" > /src/version.txt

FROM mirror.gcr.io/library/eclipse-temurin:21-jre
COPY --from=build /src/target/java-api-1.0.jar /app/app.jar
COPY --from=build /src/version.txt /app/version.txt
USER 65532:65532
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
