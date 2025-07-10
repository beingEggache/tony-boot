FROM jenkins/jenkins:2.517-jdk21

# 构建参数
ARG GID
ARG UID
ARG USER_NAME
ARG GROUP_NAME=${USER_NAME}
ARG DEBIAN_MIRROR=https://mirrors.huaweicloud.com
ARG NPM_REGISTRY=https://registry.npmmirror.com/
ARG NODE_VERSION=22

# 设置环境变量
ENV DEBIAN_FRONTEND=noninteractive
ENV NPM_CONFIG_REGISTRY=${NPM_REGISTRY}

USER root

# 1. 配置系统源和基础工具
RUN --mount=type=cache,target=/var/cache/apt \
    --mount=type=cache,target=/var/lib/apt \
    echo "deb ${DEBIAN_MIRROR}/debian bookworm main contrib non-free-firmware" > /etc/apt/sources.list && \
    echo "deb ${DEBIAN_MIRROR}/debian-security bookworm-security main contrib non-free-firmware" >> /etc/apt/sources.list && \
    echo "deb ${DEBIAN_MIRROR}/debian bookworm-updates main contrib non-free-firmware" >> /etc/apt/sources.list && \
    echo "deb ${DEBIAN_MIRROR}/debian bookworm-backports main non-free non-free-firmware contrib" >> /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
        curl \
        ca-certificates \
        gnupg \
        lsb-release

# 2. 安装 Node.js (使用 NodeSource 安装脚本 - 更快更可靠)
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash - && \
    apt-get install -y --no-install-recommends nodejs && \
    npm config set registry ${NPM_REGISTRY} && \
    npm install -g pnpm && \
    # 验证安装
    node --version && \
    npm --version && \
    pnpm --version

# 3. 创建用户和组
RUN groupadd -g ${GID} ${GROUP_NAME} && \
    useradd -m -u ${UID} -g ${GID} -s /bin/bash ${USER_NAME} && \
    # 设置用户目录权限
    chown -R ${USER_NAME}:${GROUP_NAME} /home/${USER_NAME}

# 4. 清理缓存
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    npm cache clean --force

# 切换到普通用户
USER ${USER_NAME}

# 5. 创建用户配置目录
RUN mkdir -p ~/.gradle ~/.npm ~/.pnpm-store && \
    # 配置 pnpm 存储目录
    pnpm config set store-dir ~/.pnpm-store && \
    # 配置 npm 用户级别设置
    npm config set registry ${NPM_REGISTRY}

# 设置工作目录
WORKDIR $JENKINS_HOME