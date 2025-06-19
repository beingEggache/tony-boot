#!/bin/sh
set -e

# Set WORKDIR default if not set
WORKDIR=${WORKDIR:-/app}

# Ensure log, config, and tmp directories exist
# ==================== Prepare directories ====================
echo "==================== Preparing directories ===================="
mkdir -p ${WORKDIR}/logs ${WORKDIR}/config ${WORKDIR}/tmp

# Note: unzip should be installed in Dockerfile build stage for performance.
# If unzip is not present, the extraction step will fail with a clear error.

# JVM parameter references:
# -XX:+EnableDynamicAgentLoading hide "Java agent has been loaded dynamically" warning.
# https://zhuanlan.zhihu.com/p/528949267
# JVM parameter upgrade tool: https://jacoline.dev/inspect
# JVM parameter dictionary: https://chriswhocodes.com

# When using nacos, you can add the following parameters to JVM_DEFAULT_OPTS or JVM_OPTS
# -Djava.io.tmpdir=${WORKDIR}/tmp \
# -DJM.LOG.PATH=${WORKDIR}/logs/nacos \
# -DJM.SNAPSHOT.PATH=${WORKDIR}/tmp \
# -Dcom.alibaba.nacos.naming.cache.dir=${WORKDIR}/tmp \

# ==================== Assemble JVM options ====================
echo "==================== Assembling JVM options ===================="
# Default JVM options
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

# Initialize opts variable
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

# Append JVM_APPEND_OPTS if present
if [ -n "${JVM_APPEND_OPTS}" ]
then
    opts="${opts} ${JVM_APPEND_OPTS}"
fi

# Override with JVM_OPTS if present
if [ -n "${JVM_OPTS}" ]
then
    opts="${JVM_OPTS}"
fi

unzip_opts="-n"
if [ "${OVERWRITE_CONFIG}" = 'true' ]
then
    unzip_opts="${unzip_opts} -o"
fi

# ==================== Extract configuration files ====================
echo "==================== Extracting configuration files ===================="
# Extract config files (*.yml, *.yaml, *.properties) from app.jar to config dir
if [ -f "${WORKDIR}/app.jar" ]
then
    if command -v unzip > /dev/null 2>&1; then
        echo "Extracting configuration files..."
        unzip ${unzip_opts} ${WORKDIR}/app.jar *.yml *.yaml *.properties -d ${WORKDIR}/config || {
            echo "Warning: Failed to extract configuration files. Continuing without them."
        }
    else
        echo "Error: unzip not found. Please ensure unzip is installed in the image."
        exit 1
    fi
fi

# ==================== Start application ====================
echo "==================== Starting application ===================="
echo "Starting application with JVM options: ${opts}"
exec java ${opts} -jar ${WORKDIR}/app.jar
