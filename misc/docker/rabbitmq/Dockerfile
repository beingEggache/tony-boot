FROM rabbitmq:4.1.2-management-alpine
ADD rabbitmq_delayed_message_exchange-4.1.0.ez /opt/rabbitmq/plugins/
RUN rabbitmq-plugins enable rabbitmq_stomp && rabbitmq-plugins enable rabbitmq_web_stomp && rabbitmq-plugins enable rabbitmq_delayed_message_exchange
