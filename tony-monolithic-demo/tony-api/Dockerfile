FROM openjdk:11.0.15-jre-slim as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11.0.15-jre-slim
WORKDIR application
VOLUME ["/tmp","/logs"]

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -noverify -XX:TieredStopAtLevel=1 org.springframework.boot.loader.JarLauncher"]
