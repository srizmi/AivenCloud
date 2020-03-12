package org.shariz.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.shariz.utils.Constants.*;

/**
 * helper methods.
 */
public final class HelperMethods {

    // json serializer/deserializer
    private static Gson gson = new Gson();

    private HelperMethods() {
    }

    /**
     * reads a given OS environment variable
     * @param variableName environment variable to read
     * @return value of the environment variable
     * @throws RuntimeException when given variable is not set
     */
    public static String getEnvironmentValue(String variableName) throws RuntimeException {
        final String variableValue = System.getenv(variableName);
        if (variableValue == null || variableValue.length() == 0) {
            throw new RuntimeException(String.format("%S not defined in your environment, exiting.", variableName));
        }
        return variableValue;
    }

    /**
     * centralized creator for Kafka properties to be used by the Producer and Consumer
     * @return Properties that point to a  Kafka cluster
     */
    public static Properties getKafkaProperties() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", getEnvironmentValue(BOOTSTRAP_SERVER_LIST));
        properties.put("security.protocol", "SSL");
        properties.put("ssl.endpoint.identification.algorithm", "");
        properties.put("ssl.truststore.location", getEnvironmentValue(SSL_TRUSTSTORE_LOCATION));
        properties.put("ssl.truststore.password", getEnvironmentValue(SSL_TRUSTSTORE_PASSWORD));
        properties.put("ssl.keystore.type", "PKCS12");
        properties.put("ssl.keystore.location", getEnvironmentValue(SSL_KEYSTORE_LOCATION));
        properties.put("ssl.keystore.password", getEnvironmentValue(SSL_KEYSTORE_PASSWORD));
        properties.put("ssl.key.password", getEnvironmentValue(SSL_KEY_PASSWORD));
        return properties;
    }

    /**
     * creaste Postgresql properties
     * @return Properties for a Postgresql installation
     */
    public static Properties getPostgresqlProperties() {
        final Properties properties = new Properties();
        properties.put("jdbc.url", getEnvironmentValue(JDBC_URL));
        properties.put("user", getEnvironmentValue(PG_USERNAME));
        properties.put("password", getEnvironmentValue(PG_PASSWORD));
        properties.put("ssl", "true");
        properties.put("sslmode", "verify-ca");
        properties.put("sslrootcert", getEnvironmentValue(CA_CERT_LOCATION));
        return properties;
    }

    /**
     * Uses Google GSON to serialize a given Java object
     * @param object to serialize
     * @return JSON String
     */
    public static String serialize(Object object) {
        return gson.toJson(object);
    }

    /**
     * Constructs an object of a given type from JSON
     * @param json JSON String
     * @param typeOfT type of Object to create
     * @param <T>
     * @return
     */
    public static <T> T deserialize(String json, Type typeOfT) {
        return gson.fromJson(json,typeOfT);
    }

    /**
     * reads a given file from the resources folder
     * @param fileName to read
     * @return
     * @throws IOException if file is missing, there were permission issues etc.
     */
    public static String getSqlFromFile(String fileName) throws IOException {
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
