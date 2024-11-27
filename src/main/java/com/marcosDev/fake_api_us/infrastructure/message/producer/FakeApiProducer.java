package com.marcosDev.fake_api_us.infrastructure.message.producer;

import com.marcosDev.fake_api_us.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FakeApiProducer {

    @Value("${topic.fake-api.producer.nome}")
    private String topic;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    public FakeApiProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviaRespostaCadastroProdutos(final String mensagem) {
        try{
            kafkaTemplate.send(topic, mensagem);
        } catch (Exception e) {
            throw new BusinessException("Erro ao produzir mensagem do kafka");
        }
    }


}