FROM nginxinc/nginx-unprivileged:1.27-bookworm
ARG GID
ARG UID
ARG USER_NAME
ARG GROUP_NAME=${USER_NAME}

USER root

RUN  groupadd -g ${GID} ${GROUP_NAME} \
  && useradd -m -u ${UID} -g ${GID} -s /bin/bash ${USER_NAME} \
  && chown -hR ${UID}:${GID} /sbin/nginx \
  && chown -hR ${UID}:${GID} /etc/nginx/ \
  && mkdir -p /home/$USER_NAME/nginx/html/ \
  && mkdir -p /home/$USER_NAME/nginx/cache/ \
  && mkdir -p /home/$USER_NAME/nginx/logs/ \
  && chown -R ${UID}:${GID} /home/$USER_NAME/nginx/html \
  && chown -R ${UID}:${GID} /home/$USER_NAME/nginx/cache \
  && chown -R ${UID}:${GID} /home/$USER_NAME/nginx/logs \
  && chown -R ${UID}:${GID} /var/cache/nginx \
  && chmod -R g+w /var/cache/nginx \
  && chown -R $UID:${GID} /etc/nginx \
  && chmod -R g+w /etc/nginx \
  && chmod -R a+rw /home/$USER_NAME/nginx/html \
  && chmod -R a+rw /home/$USER_NAME/nginx/cache \
  && chmod -R a+rw /home/$USER_NAME/nginx/logs
USER ${USER_NAME}