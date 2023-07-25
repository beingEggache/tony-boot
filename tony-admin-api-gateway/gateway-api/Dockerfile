FROM eclipse-temurin:11.0.19_7-jre-alpine AS builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:11.0.19_7-jre-alpine

RUN echo "Asia/Shanghai" > /etc/timezone && \
    addgroup app && \
    adduser -S app -G app -h app

USER app:app
WORKDIR app
VOLUME ["/logs"]

COPY --chown=app:app --from=builder app/dependencies/ ./
COPY --chown=app:app --from=builder app/spring-boot-loader/ ./
COPY --chown=app:app --from=builder app/snapshot-dependencies/ ./
COPY --chown=app:app --from=builder app/application/ ./
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -noverify -XX:TieredStopAtLevel=1 org.springframework.boot.loader.JarLauncher"]
