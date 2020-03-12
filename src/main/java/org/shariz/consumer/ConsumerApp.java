package org.shariz.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;

import static org.shariz.utils.Constants.SIGNUP_TOPIC;
import static org.shariz.utils.HelperMethods.getEnvironmentValue;
import static org.shariz.utils.HelperMethods.getKafkaProperties;

/**
 * Starts the consumer app and puts things in motion..
 */
public class ConsumerApp {
    // topic name to subscribe to; read from the environment.
    public static final String consumerGroupName = "signups-group";
    private SignupConsumer signupConsumer;

    public ConsumerApp() {
        final Properties properties = getKafkaProperties();
        properties.put("group.id", consumerGroupName);
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");

        final String signupTopic = getEnvironmentValue(SIGNUP_TOPIC);
        final Consumer<String, String> consumer = new KafkaConsumer<>(properties);
        signupConsumer = new SignupConsumer(consumer, signupTopic);


    }

    private void start() {
        signupConsumer.subscribe();
    }

    public static void main(String[] args) {
        new ConsumerApp().start();
    }


}
