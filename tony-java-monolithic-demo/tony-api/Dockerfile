FROM eclipse-temurin:21.0.4_7-jre-alpine AS builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM eclipse-temurin:21.0.4_7-jre-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
WORKDIR app
#-XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM 参数升级提示工具 https://jacoline.dev/inspect
# JVM 参数词典：https://chriswhocodes.com
ENV JVM_OPTS=""
ENV JVM_DEFAULT_OPTS="\
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
ENV PROPS=""
ENV ENABLE_ERROR_LOGS="true"
ENV ENABLE_GC_LOGS="false"
ENV ENABLE_JVM_LOGS="false"

EXPOSE 10000
VOLUME ["/logs","/tmp"]

COPY --from=builder app/extracted/dependencies/ ./
COPY --from=builder app/extracted/snapshot-dependencies/ ./
COPY --from=builder app/extracted/application/ ./

RUN java -XX:ArchiveClassesAtExit=app.jsa -Dspring.context.exit=onRefresh -jar app.jar

ENTRYPOINT ["sh", "-c", "java ${JVM_DEFAULT_OPTS} ${PROPS} ${JVM_OPTS} \
-XX:SharedArchiveFile=app.jsa \
$([ ${ENABLE_ERROR_LOGS} == 'true' ] && echo -n '\
-XX:ErrorFile=/logs/hs_err_pid.log \
-XX:HeapDumpPath=/logs/java_heapdump.hprof \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError') \
$([ ${ENABLE_GC_LOGS} == 'true' ] && echo -n '\
-Xlog:async \
-Xlog:gc*=info:file=/logs/gc_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:jit+compilation=info:file=/logs/jit_compile_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:safepoint=info:file=/logs/safepoint_%t.log:utctime,level,tags:filecount=10,filesize=10M') \
$([ ${ENABLE_JVM_LOGS} == 'true' ] && echo -n '\
-XX:+UnlockDiagnosticVMOptions \
-XX:LogFile=/logs/hotspot.log \
-XX:+LogCompilation \
-XX:+PrintCompilation \
-XX:+PrintInlining') \
-jar app.jar"]
