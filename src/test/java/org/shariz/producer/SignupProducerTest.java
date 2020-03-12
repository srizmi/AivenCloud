package org.shariz.producer;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignupProducerTest {

    private SignupProducer signupProducer;

    @Before
    public void setUp() throws Exception {
        MockProducer<String, String> mockProducer = new MockProducer<String, String>(true, new StringSerializer(), new StringSerializer());
        signupProducer = new SignupProducer(mockProducer, "mock_topic");
    }

    @After
    public void tearDown() throws Exception {
        signupProducer.shutdownProducer();
    }

    @Test
    public void testSendingData() {
        signupProducer.sendSignupData("{\"firstName\":\"John\",\"lastName\":\"Appleseed\",\"email\":\"jappleseed@apple.com\",\"company\":\"Apple\"}\n");
    }
}