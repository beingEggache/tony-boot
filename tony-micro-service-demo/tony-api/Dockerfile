FROM eclipse-temurin:17.0.8_7-jre-alpine AS builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.8_7-jre-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
WORKDIR app

ENV JAVA_OPTS="-Xmx2g -XX:+UseZGC"
ENV PROPS=""
ENV ENABLE_GC_LOGS="false"
ENV ENABLE_JVM_LOGS="false"

EXPOSE 8080
VOLUME ["/logs","/tmp"]

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${PROPS} \
$([ ${ENABLE_GC_LOGS} == 'true' ] && echo -n '\
-Xlog:gc*=info:file=/logs/gc.log:time,tid,level,tags:filecount=5,filesize=10M \
-XX:ErrorFile=/logs/hs_err_pid.log \
-XX:HeapDumpPath=/logs/java_heapdump.hprof \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError'\
|| echo -n '') \
$([ ${ENABLE_JVM_LOGS} == 'true' ] && echo -n '\
-XX:+UnlockDiagnosticVMOptions \
-XX:LogFile=/logs/hotspot.log \
-XX:+LogCompilation \
-XX:+PrintCompilation \
-XX:+PrintInlining'\
|| echo -n '') \
org.springframework.boot.loader.JarLauncher"]
