package com.example.springkafkastreams;


import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordCountProcessor {
    private static final Serde<String> stringSerDer = Serdes.String();

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        streamsBuilder
                .stream("word-count-input", Consumed.with(stringSerDer, stringSerDer))
                .print(Printed.toSysOut());
    }
}
