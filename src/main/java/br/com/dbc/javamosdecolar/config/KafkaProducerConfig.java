package br.com.dbc.javamosdecolar.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {


    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value(value = "${kafka.properties.sasl.jaas.config}")
    private String jaasConfig;

    @Value(value = "${kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value(value = "${kafka.properties.enable.idempotence}")
    private boolean enable;

    @Bean
    public KafkaTemplate<String,String> configKafkaTemplate(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress); // servidor
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // chave
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); // valor
        configProps.put("sasl.mechanism", saslMechanism);
        configProps.put("sasl.jaas.config", jaasConfig);
        configProps.put("security.protocol", securityProtocol);
        configProps.put("enable.idempotence" , enable);
        DefaultKafkaProducerFactory<String, String> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(configProps);
        return new KafkaTemplate<>(kafkaProducerFactory);
    }
}
