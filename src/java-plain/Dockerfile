# 阶段 1:用 JDK 把源码编译并打成 jar(这就是"先 build jar");阶段 2:只把 jar 拷进 JRE 镜像(这就是"再构建镜像")。
FROM mirror.gcr.io/library/eclipse-temurin:21-jdk AS build
ARG VERSION=dev
WORKDIR /src
COPY src ./src
RUN mkdir out \
 && javac -d out src/*.java \
 && printf 'Main-Class: Main\n' > manifest.txt \
 && jar cfm app.jar manifest.txt -C out . \
 && printf '%s' "$VERSION" > version.txt

FROM mirror.gcr.io/library/eclipse-temurin:21-jre
COPY --from=build /src/app.jar /app/app.jar
COPY --from=build /src/version.txt /app/version.txt
USER 65532:65532
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
