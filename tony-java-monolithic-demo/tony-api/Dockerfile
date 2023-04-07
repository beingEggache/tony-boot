FROM eclipse-temurin:11.0.18_10-jre-alpine as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:11.0.18_10-jre-alpine

RUN echo "Asia/Shanghai" > /etc/timezone && \
    addgroup app_group && \
    adduser -S -D -h application app app_group  && \
    mkdir -p /logs && \
    chown app:app_group /application && \
    chown app:app_group /logs && \
    chmod -R o+rw,o-x /logs

USER app:app_group
WORKDIR application
VOLUME ["/logs"]

COPY --chown=app:app_group --from=builder application/dependencies/ ./
COPY --chown=app:app_group --from=builder application/spring-boot-loader/ ./
COPY --chown=app:app_group --from=builder application/snapshot-dependencies/ ./
COPY --chown=app:app_group --from=builder application/application/ ./
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -noverify -XX:TieredStopAtLevel=1 org.springframework.boot.loader.JarLauncher"]
