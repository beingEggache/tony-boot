#!/bin/sh
set -e

# 确保日志目录存在
mkdir -p ${WORKDIR}/logs ${WORKDIR}/config ${WORKDIR}/tmp

# 安装unzip工具（如果需要）
if ! command -v unzip > /dev/null 2>&1
then
    apk add --no-cache unzip
fi

#-XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM 参数升级提示工具 https://jacoline.dev/inspect
# JVM 参数词典：https://chriswhocodes.com

# 当使用nacos 时, 可把以下参数加入到 JVM_DEFAULT_OPTS 或者 JVM_OPTS 中
#-Djava.io.tmpdir=${WORKDIR}/tmp \
#-DJM.LOG.PATH=${WORKDIR}/logs/nacos \
#-DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
#-Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \

# 默认JVM参数
JVM_DEFAULT_OPTS="\
-Djava.io.tmpdir=${WORKDIR}/tmp \
-DJM.LOG.PATH=${WORKDIR}/logs/nacos \
-DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
-Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \
-XX:SharedArchiveFile=${WORKDIR}/app.jsa \
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

# 初始化opts变量
opts="${JVM_DEFAULT_OPTS}"

if [ "${ENABLE_ERROR_LOGS}" = 'true' ]
then
    opts="\
${opts} \
-XX:ErrorFile=${WORKDIR}/logs/hs_err_pid.log \
-XX:HeapDumpPath=${WORKDIR}/logs/java_heapdump.hprof \
-XX:+ExtensiveErrorReports \
-XX:+HeapDumpOnOutOfMemoryError"
fi

if [ "${ENABLE_GC_LOGS}" = 'true' ]
then
    opts="\
${opts} \
-Xlog:async \
-Xlog:gc*=info:file=${WORKDIR}/logs/gc_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:jit+compilation=info:file=${WORKDIR}/logs/jit_compile_%t.log:utctime,level,tags:filecount=10,filesize=10M \
-Xlog:safepoint=info:file=${WORKDIR}/logs/safepoint_%t.log:utctime,level,tags:filecount=10,filesize=10M"
fi

if [ "${ENABLE_JVM_LOGS}" = 'true' ]
then
    opts="\
${opts} \
-XX:+UnlockDiagnosticVMOptions \
-XX:LogFile=${WORKDIR}/logs/hotspot.log \
-XX:+LogCompilation \
-XX:+PrintCompilation \
-XX:+PrintInlining"
fi

# 处理JVM_APPEND_OPTS - 追加到opts
if [ -n "${JVM_APPEND_OPTS}" ]
then
    opts="${opts} ${JVM_APPEND_OPTS}"
fi

# 处理JVM_OPTS - 覆盖默认设置（如果需要）
if [ -n "${JVM_OPTS}" ]
then
    opts="${JVM_OPTS}"
fi

unzip_opts="-n"
if [ "${OVERWRITE_CONFIG}" = 'true' ]
then
    unzip_opts="${unzip_opts} -o"
fi

# 解压配置文件 到${WORKDIR}/config
if [ -f "${WORKDIR}/app.jar" ]
then
    echo "Extracting configuration files..."
    unzip ${unzip_opts} ${WORKDIR}/app.jar *.yml *.yaml *.properties -d ${WORKDIR}/config || {
        echo "Warning: Failed to extract configuration files. Continuing without them."
    }
fi

echo "Starting application with JVM options: ${opts}"
exec java ${opts} -jar ${WORKDIR}/app.jar
