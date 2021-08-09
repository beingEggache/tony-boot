# First stage: JDK 11 with modules required for Spring Boot
FROM debian:stretch as packager

ENV JDK_VERSION="11.0.8"
ENV JDK_URL="https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz"
ENV JDK_ARJ_FILE="openjdk-${JDK_VERSION}.tar.gz"

# target JDK installation names
ENV OPT="/opt"

#downloaded jdk path, execute jlink need this.
ENV JDK_DIR_NAME="jdk-11"

ENV JAVA_HOME="${OPT}/${JDK_DIR_NAME}"
ENV JAVA_MINIMAL="${OPT}/java-minimal"

# downlodad JDK to the local file
ADD "$JDK_URL" "/$JDK_ARJ_FILE"

# extract JDK and add to PATH
RUN { \
        echo "Unpack downloaded JDK to ${JAVA_HOME}/:" && \
        mkdir -p "$OPT" && \
        tar xf "$JDK_ARJ_FILE" -C "$OPT" ; \
    }
ENV PATH="$PATH:$JAVA_HOME/bin"

RUN { \
        java --version ; \
        echo "jlink version:" && \
        jlink --version ; \
    }

# build modules distribution
RUN jlink \
    --verbose \
    --add-modules \
        java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,jdk.unsupported \
        # java.naming - javax/naming/NamingException
        # java.desktop - java/beans/PropertyEditorSupport
        # java.management - javax/management/MBeanServer
        # java.security.jgss - org/ietf/jgss/GSSException
        # java.instrument - java/lang/instrument/IllegalClassFormatException
        # jdk.unsupported - support netty low-level api
    --compress 2 \
    --strip-debug \
    --no-header-files \
    --no-man-pages \
    --output "$JAVA_MINIMAL"

# Second stage, add only our minimal "JRE" distr
FROM debian:stretch

RUN echo " " > /etc/apt/sources.list; \
    echo "deb http://mirrors.aliyun.com/debian stretch  main" >> /etc/apt/sources.list; \
    echo "deb http://mirrors.aliyun.com/debian stretch -updates main" >> /etc/apt/sources.list; \
    set -eux; \
    apt-get clean; \
	apt-get update; \
	apt-get install -y --no-install-recommends --fix-broken \
		bzip2 \
		unzip \
		xz-utils \
		\
# java.lang.UnsatisfiedLinkError: /usr/local/openjdk-11/lib/libfontmanager.so: libfreetype.so.6: cannot open shared object file: No such file or directory
# java.lang.NoClassDefFoundError: Could not initialize class sun.awt.X11FontManager
# https://github.com/docker-library/openjdk/pull/235#issuecomment-424466077
		fontconfig libfreetype6 ttf-dejavu \
	; \
	rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/opt/java-minimal
ENV PATH="$PATH:$JAVA_HOME/bin"

COPY --from=packager "$JAVA_HOME" "$JAVA_HOME"
