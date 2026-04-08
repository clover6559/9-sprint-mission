FROM amazoncorretto:17 AS build

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=v1.2-M8 \
    JVM_OPTS=""

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./
COPY *.gradle ./
COPY gradle.properties* ./

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon --quiet

COPY src ./src

RUN ./gradlew build --no-daemon -x test --quiet

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

ENV PROJECT_NAME=discodeit \
    PROJECT_VERSION=v1.2-M8 \
    JVM_OPTS=""

COPY --from=build /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar app.jar

EXPOSE 80

ENTRYPOINT ["sh", "-c", "java ${JVM_OPTS} -jar app.jar"]