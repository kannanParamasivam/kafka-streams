package com.example.springkafkastreams.kstream;


import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TopologyDescription;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class WordCountProcessor {
    private static final Serde<String> stringSerDe = Serdes.String();

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {

        KTable<String, Long> KTableWordCount = streamsBuilder
                .stream("word-count-input", Consumed.with(stringSerDe, stringSerDe))
                .mapValues(value -> value.toLowerCase())
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .peek((key, value) -> System.out.println("flat map values: " + "key: " + key + " value: " + value))
                .groupBy((key, word) -> word, Grouped.with(stringSerDe, stringSerDe))
                .count(Materialized.as("word-counts"));

        KTableWordCount.toStream()
                .peek((key, value) -> System.out.println("KTable values: " + "key: " + key + " value: " + value))
                .to("word-count-output", Produced.with(stringSerDe, Serdes.Long()));
    }

}







