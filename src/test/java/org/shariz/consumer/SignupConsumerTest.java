package org.shariz.consumer;

import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.shariz.producer.SignupProducer;

import static org.junit.Assert.*;

public class SignupConsumerTest {

    private SignupConsumer signupConsumer;

    @Before
    public void setUp() throws Exception {
        MockConsumer<String, String> mockProducer = new MockConsumer(OffsetResetStrategy.EARLIEST);

    }


}