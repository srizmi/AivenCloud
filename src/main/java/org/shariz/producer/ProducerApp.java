package org.shariz.producer;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

import static org.shariz.utils.Constants.SIGNUP_TOPIC;
import static org.shariz.utils.HelperMethods.getEnvironmentValue;
import static org.shariz.utils.HelperMethods.getKafkaProperties;

/**
 * Runs an HttpServer that listens for signup requests and forwards the requests to Kafka for processing.
 */
public class ProducerApp implements HttpHandler {

    private SignupProducer signupProducer = null;
    private static Logger logger = LoggerFactory.getLogger(ProducerApp.class);

    private static final int HTTP_PORT = 8080;

    /**
     * Constructor
     */
    public ProducerApp() {
        // get the common Kafka properties and add key,value String serializers
        final Properties properties = getKafkaProperties();
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        final String signupTopic = getEnvironmentValue(SIGNUP_TOPIC);

        final Producer<String, String> producer = new KafkaProducer(properties);
        this.signupProducer = new SignupProducer(producer, signupTopic);
    }

    /**
     * Starts the HTTP listener that's waiting for client requests
     * @throws IOException
     */
    public void startHttpServer() throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
        final HttpContext context = httpServer.createContext("/api/submit_request");
        context.setHandler(this);
        httpServer.start();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // only accept POST method from the http client
        if ("POST".equals(httpExchange.getRequestMethod()) == false) {
            String badMethodMessage = "405 : Method not allowed!";
            writeResponse(httpExchange, badMethodMessage, 405);
            return;
        }

        // read the request body that contains signup request JSON string and submit to Kafka for processing
        try (Scanner scanner = new Scanner(httpExchange.getRequestBody(), StandardCharsets.UTF_8.name())) {
            String json = scanner.useDelimiter("\\A").next();
            signupProducer.sendSignupData(json);
        }

        // let the HTTP client know that the request has been submitted.
        writeResponse(httpExchange, "submitted", 200);
        return;
    }

    /**
     * produces the http response
     *
     * @param exchange HTTP
     * @param response
     * @param code
     * @throws IOException
     */
    private void writeResponse(HttpExchange exchange, String response, int code) throws IOException {
        exchange.sendResponseHeaders(code, response.length());//response code and length
        final OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args) {
        final ProducerApp producerApp = new ProducerApp();
        try {
            producerApp.startHttpServer();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
