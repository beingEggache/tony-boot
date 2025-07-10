FROM eclipse-temurin:21.0.7_6-jdk-alpine AS jre-builder

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

RUN apk add --no-cache \
    ca-certificates \
    tzdata \
    fontconfig \
    ttf-dejavu \
    && rm -rf /var/cache/apk/* /tmp/*
	
COPY --from=jre-builder /custom-jre /opt/jre

ENV PATH="/opt/jre/bin:${PATH}" \
    JAVA_HOME="/opt/jre" \
    LANG=C.UTF-8 \
    TZ=Asia/Shanghai

RUN java -Xshare:dump

CMD ["java", "-version"]