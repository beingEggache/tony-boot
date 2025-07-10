FROM sonatype/nexus3:3.81.1-java17-alpine

ARG GID
ARG UID
ARG USER_NAME
ARG GROUP_NAME=${USER_NAME}
USER root

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories \
    && apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && apk --no-cache add shadow \
    && groupadd -g ${GID} ${GROUP_NAME} \
    && useradd -u ${UID} -g ${GID} ${USER_NAME} \
    && apk del shadow
ENV TZ=Asia/Shanghai
USER ${USER_NAME}

