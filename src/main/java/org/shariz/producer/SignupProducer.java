package org.shariz.producer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignupProducer {

    // We use the Producer interface instead of the KafkaProducer implementation to make it possible to inject a MockProducer.
    private Producer<String, String> kafkaProducer = null;
    private Logger logger = LoggerFactory.getLogger(SignupProducer.class);
    // Topic to send the messages to
    private String signupTopic = null;

    /**
     * Constructor.
     *
     * @param producer    a real or mock KafkaProducer
     * @param signupTopic topic to send the signup messages to
     */
    public SignupProducer(Producer producer, String signupTopic) {
        this.kafkaProducer = producer;
        this.signupTopic = signupTopic;
    }

    /**
     * Send the message
     *
     * @param signupJson - serialized signup request
     */
    public void sendSignupData(String signupJson) {
        logger.info("received message to send:" + signupJson);
        kafkaProducer.send(new ProducerRecord<String, String>(signupTopic, signupJson));
    }

    /**
     * Shuts down the producer
     */
    public void shutdownProducer() {
        kafkaProducer.close();
    }
}
