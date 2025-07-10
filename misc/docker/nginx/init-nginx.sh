#!/usr/bin/env bash

nginx_dir="/usr/local/data/nginx"
nginx_conf_dir="/usr/local/data/nginx/conf.d"
nginx_html_dir="/usr/local/data/nginx/html"
nginx_log_dir="/usr/local/data/nginx/logs"

mkdir -p {${nginx_conf_dir},${nginx_html_dir},${nginx_log_dir}}

echo 'created nginx required directories'
echo "
user  nginx;
# One process for each CPU-Core
worker_processes  4;

pid        /var/run/nginx.pid;

events {
    worker_connections  65535;
    multi_accept        on;
    use                 epoll;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

	log_format  main  '\$remote_addr - \$remote_user [\$time_local] \"\$request\" '
                      '\$status \$body_bytes_sent \"\$http_referer\" '
                      '\"\$http_user_agent" "\$http_x_forwarded_for\"';

    log_format  compression	  '\$remote_addr - \$remote_user [\$time_local] \"\$request\" '
							  '\$status \$body_bytes_sent \"\$http_referer\" '
							  '\$http_user_agent \$http_x_forwarded_for'
							  'request_time:\$request_time upstream[addr:\$upstream_addr status:\$upstream_status
							  response_time:\$upstream_response_time]';

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    gzip  on;

	proxy_cache_path /usr/share/nginx/html/cache keys_zone=cache:100m inactive=7d max_size=10g;
    include /etc/nginx/conf.d/*.conf;
}
" > ${nginx_dir}/nginx.conf
echo 'created nginx.conf'