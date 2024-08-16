FROM eclipse-temurin:21.0.4_7-jre-alpine AS builder
ARG WORKDIR=/app
WORKDIR ${WORKDIR}
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.4_7-jre-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
ARG WORKDIR=/app
WORKDIR ${WORKDIR}
#-XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM 参数升级提示工具 https://jacoline.dev/inspect
# JVM 参数词典：https://chriswhocodes.com
ENV JVM_OPTS=""
# 当使用nacos 时, 可把以下参数加入到 JVM_DEFAULT_OPTS 或者 JVM_OPTS 中
#-Djava.io.tmpdir=${WORKDIR}/tmp \
#-DJM.LOG.PATH=${WORKDIR}/log/nacos \
#-DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
#-Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \
ENV JVM_DEFAULT_OPTS="\
-Djava.io.tmpdir=${WORKDIR}/tmp \
-XX:+EnableDynamicAgentLoading \
-XX:+SegmentedCodeCache \
-XX:+UseStringDeduplication \
-XX:+UnlockDiagnosticVMOptions \
-XX:+UnlockExperimentalVMOptions \
-XX:-OmitStackTraceInFastThrow \
-XX:+AlwaysPreTouch \
-XX:+DisableExplicitGC \
-XX:+UseCompressedOops \
-XX:GuaranteedSafepointInterval=0 \
-XX:+SafepointTimeout \
-XX:SafepointTimeoutDelay=1000"
ENV PROFILE="dev"
ENV ENABLE_ERROR_LOGS="true"
ENV ENABLE_GC_LOGS="false"
ENV ENABLE_JVM_LOGS="false"
ENV OVERWRITE_CONFIG="false"

ENV GID=0
ENV UID=0
ENV USER_NAME="root"
ENV GROUP_NAME=${USER_NAME}
USER ${USER_NAME}

ARG PORT=10000
EXPOSE ${PORT}

VOLUME ["${WORKDIR}/logs", "${WORKDIR}/tmp", "${WORKDIR}/config"]

COPY --from=builder ${WORKDIR}/extracted/dependencies/ ./
COPY --from=builder ${WORKDIR}/extracted/snapshot-dependencies/ ./
COPY --from=builder ${WORKDIR}/extracted/application/ ./

RUN java -XX:ArchiveClassesAtExit=app.jsa -Dspring.context.exit=onRefresh -jar app.jar

ENTRYPOINT ["sh", "-c", "unzip -n $([ ${OVERWRITE_CONFIG} == 'true' ] && echo '-o') app.jar *.yml *.yaml *.properties -d config && \
java ${JVM_DEFAULT_OPTS} -Dspring.profiles.active=${PROFILE} ${JVM_OPTS} \
-XX:SharedArchiveFile=app.jsa \
$([ ${ENABLE_ERROR_LOGS} == 'true' ] && echo -n '\
-XX:ErrorFile=${WORKDIR}/logs/hs_err_pid.log \
-XX:HeapDumpPath=${WORKDIR}/logs/java_heapdump.hprof \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError') \
$([ ${ENABLE_GC_LOGS} == 'true' ] && echo -n '\
-Xlog:async \
-Xlog:gc*=info:file=${WORKDIR}/logs/gc_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:jit+compilation=info:file=${WORKDIR}/logs/jit_compile_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:safepoint=info:file=${WORKDIR}/logs/safepoint_%t.log:utctime,level,tags:filecount=10,filesize=10M') \
$([ ${ENABLE_JVM_LOGS} == 'true' ] && echo -n '\
-XX:+UnlockDiagnosticVMOptions \
-XX:LogFile=${WORKDIR}/logs/hotspot.log \
-XX:+LogCompilation \
-XX:+PrintCompilation \
-XX:+PrintInlining') \
-jar app.jar"]
