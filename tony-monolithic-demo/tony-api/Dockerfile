FROM eclipse-temurin:17.0.8_7-jre-alpine AS builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.8_7-jre-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
WORKDIR app

ENV JAVA_OPTS="\
-noverify \
-XX:TieredStopAtLevel=1 \
-Xmn2g \
-Xms2g \
-Xmx2g \
-Xlog:gc*=info:file=/logs/gc.log:time,tid,level,tags:filecount=5,filesize=10M \
-XX:ErrorFile=/logs/hs_err_pid.log \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=/logs/java_pid.hprof"

ENV PROP=""
EXPOSE 8080

COPY --chown=app:app --from=builder app/dependencies/ ./
COPY --chown=app:app --from=builder app/spring-boot-loader/ ./
COPY --chown=app:app --from=builder app/snapshot-dependencies/ ./
COPY --chown=app:app --from=builder app/application/ ./
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} ${PROP} org.springframework.boot.loader.JarLauncher"]
