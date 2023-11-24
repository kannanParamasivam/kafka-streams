package com.example.springkafkastreams.kstream;

import com.example.springkafkastreams.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Message sendMessage(Message message) {
        try {

            SendResult<String, String> sendResult = kafkaTemplate.send(
                    "word-count-input",
                    message.getKey(),
                    message.getMessage()).get();

            return new Message(sendResult.getProducerRecord().key(),
                    sendResult.getProducerRecord().value());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
