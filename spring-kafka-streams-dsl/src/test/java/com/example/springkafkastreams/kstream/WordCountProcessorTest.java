package com.example.springkafkastreams.kstream;

import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WordCountProcessorTest {

    @Test
    void shouldProduceCount_whenProcessed_givenInputMessages() {
        //given
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        new WordCountProcessor().buildPipeline(streamsBuilder);
        Topology topology = streamsBuilder.build();

        //when
        try (TopologyTestDriver topologyTestDriver = new TopologyTestDriver(topology, new Properties())) {
            TestInputTopic<String, String> inputTopic = topologyTestDriver.createInputTopic(
                    "word-count-input",
                    new StringSerializer(),
                    new StringSerializer());

            TestOutputTopic<String, Long> outputTopic = topologyTestDriver.createOutputTopic(
                    "word-count-output",
                    new StringDeserializer(),
                    new LongDeserializer());

            inputTopic.pipeInput("key", "Hello World");
            inputTopic.pipeInput("key", "hello");

            //then

            assertEquals(
                    Arrays.asList(
                            new KeyValue<>("hello", 1L),
                            new KeyValue<>("world", 1L),
                            new KeyValue<>("hello", 2L)),
                    outputTopic.readKeyValuesToList());
        }
    }
}