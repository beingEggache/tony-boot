FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jre-alpine
RUN echo "Asia/Shanghai" > /etc/timezone
WORKDIR app
#-XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM 参数升级提示工具 https://jacoline.dev/inspect
# JVM 参数词典：https://chriswhocodes.com
ENV JAVA_OPTS="\
-Xmx2g \
-XX:+UseZGC \
-XX:+ZGenerational \
-XX:+EnableDynamicAgentLoading \
-XX:+SegmentedCodeCache \
-XX:+UseStringDeduplication \
-XX:+UnlockDiagnosticVMOptions \
-XX:+UnlockExperimentalVMOptions \
-XX:-OmitStackTraceInFastThrow \
-XX:+AlwaysPreTouch \
-XX:+DisableExplicitGC \
-XX:+UseCompressedOops \
-XX:+UseStringDeduplication \
-XX:GuaranteedSafepointInterval=0 \
-XX:+UseCountedLoopSafepoints \
-XX:+SafepointTimeout \
-XX:SafepointTimeoutDelay=1000 \
--add-opens java.base/java.lang=ALL-UNNAMED \
--add-opens java.base/java.io=ALL-UNNAMED \
--add-opens java.base/java.math=ALL-UNNAMED \
--add-opens java.base/java.net=ALL-UNNAMED \
--add-opens java.base/java.nio=ALL-UNNAMED \
--add-opens java.base/java.security=ALL-UNNAMED \
--add-opens java.base/java.text=ALL-UNNAMED \
--add-opens java.base/java.time=ALL-UNNAMED \
--add-opens java.base/java.util=ALL-UNNAMED \
--add-opens java.base/jdk.internal.access=ALL-UNNAMED \
--add-opens java.base/jdk.internal.misc=ALL-UNNAMED"
ENV PROPS=""
ENV ENABLE_ERROR_LOGS="true"
ENV ENABLE_GC_LOGS="false"
ENV ENABLE_JVM_LOGS="false"

EXPOSE 10000
VOLUME ["/logs","/tmp"]

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${PROPS} \
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
org.springframework.boot.loader.launch.JarLauncher"]
