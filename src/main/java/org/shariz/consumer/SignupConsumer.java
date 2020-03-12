package org.shariz.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.shariz.consumer.db.SignupDao;
import org.shariz.model.SignupRequest;
import org.shariz.utils.HelperMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.shariz.utils.Constants.*;
import static org.shariz.utils.HelperMethods.getEnvironmentValue;
import static org.shariz.utils.HelperMethods.getKafkaProperties;

/**
 * consumes messages from the signups topic. When a message is received, checks if the associated email is already used and if not,
 * records the signup request in the database.
 */
public class SignupConsumer {


    // topic name to subscribe to; read from the environment.
    private String signupTopic = null;

    private Consumer<String, String> consumer = null;
    private Logger logger = LoggerFactory.getLogger(SignupConsumer.class);
    private SignupDao signupDao = new SignupDao();


    public static final String consumerGroupName = "signups-group";

    /**
     * Constructor.
     * creates the Kafka consumer
     */
    public SignupConsumer(Consumer consumer, String signupTopic) {
        this.consumer = consumer;
        this.signupTopic = signupTopic;
    }

    /**
     * starts the subscription and polls the topic for messages.
     */
    public void subscribe() {
        consumer.subscribe(Arrays.asList(signupTopic));
        while (true) {
            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1000L));
            for (ConsumerRecord<String, String> record : records) {
                final String signupJson = record.value();
                final SignupRequest signupRequest = HelperMethods.deserialize(signupJson, SignupRequest.class);
                final String email = signupRequest.getEmail();
                logger.info(String.format("Signup Request received for email: %s", email));
                try {
                    if (signupDao.isAlreadyRegistered(email)) {
                        logger.info(String.format("%s is already registered with an account.", email));
                        continue;
                    }
                    logger.info(String.format("Registering %s  ..", email));
                    signupDao.persistSignupRequest(signupRequest);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
