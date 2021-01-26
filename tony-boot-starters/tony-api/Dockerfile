FROM openjdk:11.0.9.1-jre-buster
VOLUME ["/tmp","/secrets","/logs"]
ARG DEPENDENCY=target/unpack
COPY ["$DEPENDENCY/app","/app"]
ENV TZ='Asia/Shanghai' LANG='C.UTF-8'
EXPOSE 8080
ENTRYPOINT ["java","-noverify","-cp","app:app/libs/*","com.tony.api.App"]
