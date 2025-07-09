FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21 AS builder

ARG WORKDIR=/app
WORKDIR ${WORKDIR}

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN java -Djarmode=tools -jar ${WORKDIR}/app.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.7_6-jre-alpine-3.21

# 安装时区和 unzip 工具
RUN apk add --no-cache tzdata unzip && \
    echo "Asia/Shanghai" > /etc/timezone && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

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

RUN java \
    -XX:+TieredCompilation \
    -XX:TieredStopAtLevel=1 \
    -XX:ArchiveClassesAtExit=${WORKDIR}/app.jsa \
    -Dspring.context.exit=onRefresh \
    -Dspring.main.banner-mode=off \
    -Dspring.output.ansi.enabled=never \
    -jar ${WORKDIR}/app.jar
COPY --chmod=755 docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["sh", "-c", "docker-entrypoint.sh"]
