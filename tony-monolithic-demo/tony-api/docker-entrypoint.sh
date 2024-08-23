#!/bin/sh
set -e

#-XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM 参数升级提示工具 https://jacoline.dev/inspect
# JVM 参数词典：https://chriswhocodes.com

# 当使用nacos 时, 可把以下参数加入到 JVM_DEFAULT_OPTS 或者 JVM_OPTS 中
#-Djava.io.tmpdir=${WORKDIR}/tmp \
#-DJM.LOG.PATH=${WORKDIR}/log/nacos \
#-DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
#-Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \

opts="\
-Djava.io.tmpdir=${WORKDIR}/tmp \
-DJM.LOG.PATH=${WORKDIR}/log/nacos \
-DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
-Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \
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
-XX:SafepointTimeoutDelay=1000 \
-Dspring.profiles.active=${PROFILE} \
-Dserver.port=${PORT}"

if [ "${ENABLE_ERROR_LOGS}" == 'true' ]
then
    opts="\
${opts} \
-XX:ErrorFile=${WORKDIR}/logs/hs_err_pid.log \
-XX:HeapDumpPath=${WORKDIR}/logs/java_heapdump.hprof \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError"
fi

if [ "${ENABLE_GC_LOGS}" == 'true' ]
then
    opts="\
${opts} \
-Xlog:async \
-Xlog:gc*=info:file=${WORKDIR}/logs/gc_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:jit+compilation=info:file=${WORKDIR}/logs/jit_compile_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:safepoint=info:file=${WORKDIR}/logs/safepoint_%t.log:utctime,level,tags:filecount=10,filesize=10M"
fi

if [ "${ENABLE_JVM_LOGS}" == 'true' ]
then
    opts="\
${opts} \
-XX:+UnlockDiagnosticVMOptions \
-XX:LogFile=${WORKDIR}/logs/hotspot.log \
-XX:+LogCompilation \
-XX:+PrintCompilation \
-XX:+PrintInlining"
fi

if [ "${JVM_APPEND_OPTS}" ]
then
    opts="\
${opts} \
${JVM_APPEND_OPTS}"
fi

if [ "${JVM_OPTS}" ]
then
    opts="${JVM_OPTS}"
fi

unzip_opts="-n"
if [ "${OVERWRITE_CONFIG}" == 'true' ]
then
    unzip_opts="${unzip_opts} -o"
fi

# 解压配置文件 到${WORKDIR}/config
sh -c "unzip ${unzip_opts} ${WORKDIR}/app.jar *.yml *.yaml *.properties -d ${WORKDIR}/config"

echo "${opts}"
sh -c "java ${opts} -jar ${WORKDIR}/app.jar"
