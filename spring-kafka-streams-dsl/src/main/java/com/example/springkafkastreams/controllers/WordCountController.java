package com.example.springkafkastreams.controllers;

import com.example.springkafkastreams.kstream.MessageProducer;
import com.example.springkafkastreams.model.Message;
import com.example.springkafkastreams.model.WordCountResponse;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/word-count")
public class WordCountController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private final MessageProducer messageProducer;
    ReadOnlyKeyValueStore<Object, Object> wordCountStore;

    WordCountController(StreamsBuilderFactoryBean streamsBuilderFactoryBean, MessageProducer messageProducer){

        this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
        this.messageProducer = messageProducer;
    }

    @GetMapping("/{word}")
    public WordCountResponse getWordCount(@PathVariable String word) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        assert kafkaStreams != null;
        wordCountStore = kafkaStreams.store(StoreQueryParameters.fromNameAndType("word-counts",
                QueryableStoreTypes.keyValueStore()));

        WordCountResponse wordCountResponse = new WordCountResponse(word, (Long) wordCountStore.get(word));
        return wordCountResponse;
    }

    @PostMapping
    public Message postMessage(@RequestBody Message message) {
        return messageProducer.sendMessage(message);
    }
}
