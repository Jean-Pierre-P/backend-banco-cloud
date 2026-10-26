package cl.duoc.bancocloud.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    private final RabbitMqProperties properties;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Exchanges: Principal y Dead Letter Exchange (DLX)
    @Bean
    public DirectExchange mainExchange() {
        return ExchangeBuilder.directExchange(properties.getExchanges().getMain()).durable(true).build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(properties.getExchanges().getDlx()).durable(true).build();
    }

    // Cola Dead Letter (DLQ) y su Binding
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(properties.getQueues().getDlq()).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(properties.getRoutingKeys().getDlq());
    }

    // Caso de Uso 1: Emisión y Cambio de Estado de Cheques -> Con desvío a DLQ en caso de error
    @Bean
    public Queue emisionQueue() {
        return QueueBuilder.durable(properties.getQueues().getEmision())
                .withArgument("x-dead-letter-exchange", properties.getExchanges().getDlx())
                .withArgument("x-dead-letter-routing-key", properties.getRoutingKeys().getDlq())
                .build();
    }

    @Bean
    public Binding emisionBinding() {
        return BindingBuilder.bind(emisionQueue())
                .to(mainExchange())
                .with(properties.getRoutingKeys().getEmision());
    }

    // Caso de Uso 2: Devolución de Cheques -> Con desvío a DLQ en caso de error
    @Bean
    public Queue devolucionQueue() {
        return QueueBuilder.durable(properties.getQueues().getDevolucion())
                .withArgument("x-dead-letter-exchange", properties.getExchanges().getDlx())
                .withArgument("x-dead-letter-routing-key", properties.getRoutingKeys().getDlq())
                .build();
    }

    @Bean
    public Binding devolucionBinding() {
        return BindingBuilder.bind(devolucionQueue())
                .to(mainExchange())
                .with(properties.getRoutingKeys().getDevolucion());
    }

    // Caso de Uso 3: Función Transversal (Autorización de Acceso al Portal) -> Con desvío a DLQ
    @Bean
    public Queue accesoQueue() {
        return QueueBuilder.durable(properties.getQueues().getAcceso())
                .withArgument("x-dead-letter-exchange", properties.getExchanges().getDlx())
                .withArgument("x-dead-letter-routing-key", properties.getRoutingKeys().getDlq())
                .build();
    }

    @Bean
    public Binding accesoBinding() {
        return BindingBuilder.bind(accesoQueue())
                .to(mainExchange())
                .with(properties.getRoutingKeys().getAcceso());
    }
}