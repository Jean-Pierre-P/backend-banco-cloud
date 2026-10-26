package cl.duoc.bancocloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.rabbitmq")
public class RabbitMqProperties {
    private Exchanges exchanges;
    private Queues queues;
    private RoutingKeys routingKeys;

    @Data
    public static class Exchanges {
        private String main;
        private String dlx;
    }

    @Data
    public static class Queues {
        private String emision;
        private String devolucion;
        private String acceso;
        private String dlq;
    }

    @Data
    public static class RoutingKeys {
        private String emision;
        private String devolucion;
        private String acceso;
        private String dlq;
    }
}