FROM eclipse-temurin:21.0.5_11-jre-alpine AS builder

ARG WORKDIR=/app
WORKDIR ${WORKDIR}

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN java -Djarmode=tools -jar ${WORKDIR}/app.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.5_11-jre-alpine

RUN echo "Asia/Shanghai" > /etc/timezone

ARG WORKDIR=/app
ENV WORKDIR=${WORKDIR}
WORKDIR ${WORKDIR}

ENV JVM_OPTS=""
ENV JVM_APPEND_OPTS=""
ENV PROFILE="dev"
ENV ENABLE_ERROR_LOGS="true"
ENV ENABLE_GC_LOGS="false"
ENV ENABLE_JVM_LOGS="false"
ENV OVERWRITE_CONFIG="false"

ARG PORT=10000
ENV PORT=${PORT}
EXPOSE ${PORT}

VOLUME ["${WORKDIR}/logs", "${WORKDIR}/tmp", "${WORKDIR}/config"]

COPY --from=builder ${WORKDIR}/extracted/dependencies/ ./
COPY --from=builder ${WORKDIR}/extracted/snapshot-dependencies/ ./
COPY --from=builder ${WORKDIR}/extracted/application/ ./

RUN java -XX:ArchiveClassesAtExit=${WORKDIR}/app.jsa -Dspring.context.exit=onRefresh -jar ${WORKDIR}/app.jar
COPY --chmod=755 docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]
