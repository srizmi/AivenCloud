# Aiven Cloud Demonstration Application

This project demonstrates utilizing Aiven services to implement a signup request processing system. The demo consists of two components: 
1. A Kafka Producer (ProducerApp) that spawns an HTTP listener for incoming requests and then publishes it to a topic on Aiven cluster.
2. A Kafka Consumer that polls a topic on Aiven cluster, and persists the requests to Postgresql running at Aiven.

### ProducerApp
The ProducerApp listens on address http://localhost:8080/api/submit_request for incoming signup requests. When an HTTP client connects and sends the request as JSON body, the App forwards the request to Kafka for processing. The JSON message is structured as follows:

```json
{
   "firstName":"John",
   "lastName":"Appleseed",
   "email":"jappleseed@apple.com",
   "company":"Apple"
}  
```

### ConsumerApp
The ConsumerApp subscribes to the topic the ProducerApp is sending messages to. The received message is deserialized into a Java object.  The ConsumerApp checks to see if the email associated with the request is already registered in the system and if not, persists the request to Postgresql running on the Aiven Cloud. 

The SQL statements that interact with the Posgresql database are externalized and stored in the resources/sql folder.
 

## Instructions for building and running the Applications

The following links describe how to set up your Aiven Kafka and Postgresql services.
 
[Getting started with Aiven Kafka](https://help.aiven.io/en/articles/489572-getting-started-with-aiven-kafka?utm_campaign=1335295399&utm_medium=cpc&utm_term=%2Baiven%2Bkafka&utm_source=google&utm_matchtype=b&utm_target=kwd-428389924469&utm_content=386043903815&utm_adgroup=53543243036&gclid=Cj0KCQiAkePyBRCEARIsAMy5Scskqk3N3sQWPhGXrcTn-697s66CPaGoMyeMVttgjkf8GsNzxEzgCQYaAl-YEALw_wcB)

[Getting started with Aiven PostgreSQL](https://help.aiven.io/en/articles/489573-getting-started-with-aiven-postgresql)

Please follow the above links for instructions to download the keys and certificates and to generate the required keystore that's used by both the ProducerApp and the ConsumerApp. The ConsumerApp also needs the Postgresql username and password as well. You can retrieve them from the Postgresql link above.

The ddl folder contains the file **create_schema.sql**  to create the Postgresql schema and the table.   

Both applications store all credentials and certificates in environment variables for security reasons. the bin folder contains template files that need populated before you can run the applications. 

The ProducerApp makes use of environment variables defined in the my-producer-env-template file. Please make a copy of this file, rename it to my-producer-env and change the placeholder values.

The ConsumerApp makes use of environment variables defined in the my-consumer-env-template file. Please make a copy of this file, rename it to my-consumer-env and change the placeholder values.

Gradle is used to compile and build. The exact Gradle and JVM versions that were used to build the system are below.  
```
------------------------------------------------------------
Gradle 6.2.2
------------------------------------------------------------

Build time:   2020-03-04 08:49:31 UTC
Revision:     7d0bf6dcb46c143bcc3b7a0fa40a8e5ca28e5856

Kotlin:       1.3.61
Groovy:       2.5.8
Ant:          Apache Ant(TM) version 1.10.7 compiled on September 1 2019
JVM:          13.0.2 (Oracle Corporation 13.0.2+8)
OS:           Mac OS X 10.15.2 x86_64
```

Please run build_aiven_cloud.sh in the root of the solution to compile and build the solution. The jar file **build/libs/AivenCloud-1.0.jar** contains all required libraries to run both the ProducerApp and the ConsumerApp.

To start the ProducerApp, please switch to the bin folder and  execute run_producer.sh. The my-producer-env is sourced in to make the OS environment variables available to the ProducerApp.  

To start the ConsumerApp, please switch to the bin folder and  execute run_consumer.sh. The my-consumer-env is sourced in to make the OS environment variables available to the ConsumerApp.  

### Testing
In the bin folder, you'll find send_signup_request.sh file which uses **curl** to send a test message to the ProducerApp http listener. This sets things in motion and does the integration testing.

