FROM eclipse-temurin:21.0.8_9-jdk-alpine AS jre-builder
RUN apk add --no-cache binutils upx
RUN jlink \
    --add-modules java.base,java.logging,java.management,java.naming,java.security.jgss,java.instrument,java.sql,java.net.http,java.desktop,java.rmi,jdk.crypto.ec,jdk.unsupported,jdk.management \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --compress=2 \
    --output /custom-jre
	
RUN find /custom-jre -name "*.so" -exec strip {} \; || true && \
    find /custom-jre -name "*.so" -exec upx --best {} \; 2>/dev/null || true

FROM alpine:3.21
# 安装必要的包并设置时区
RUN apk add --no-cache \
    ca-certificates \
    tzdata \
    fontconfig \
    ttf-dejavu \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && rm -rf /var/cache/apk/* /tmp/*

# 复制自定义JRE
COPY --from=jre-builder /custom-jre /opt/jre

# 设置环境变量
ENV PATH="/opt/jre/bin:${PATH}" \
    JAVA_HOME="/opt/jre" \
    LANG=C.UTF-8 \
    TZ=Asia/Shanghai

# 生成共享类数据存档以提高启动性能
RUN java -Xshare:dump

CMD ["java", "-version"]