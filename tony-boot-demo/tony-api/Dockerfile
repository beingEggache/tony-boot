FROM 127.0.0.1:8082/publisher/minimal-jre11:latest
VOLUME ["/tmp","/secrets","/logs"]
ARG DEPENDENCY=target/unpack
COPY ["$DEPENDENCY/app","/app"]
ENV TZ='Asia/Shanghai' LANG='C.UTF-8'
EXPOSE 8080
ENTRYPOINT ["java","-noverify","-cp","app:app/libs/*","com.tony.api.ApiWebAppKt"]
