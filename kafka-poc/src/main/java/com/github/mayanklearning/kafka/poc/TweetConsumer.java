package com.github.mayanklearning.kafka.poc;

import com.github.mayanklearning.models.Product;
import com.github.mayanklearning.models.Tweet;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class TweetConsumer {


    public static KafkaConsumer<String, String> createConsumer(String topic){

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "finalized-app";

        //consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe to topics
        //consumer.subscribe(Collections.singleton(topic));
        // OR
        consumer.subscribe(Arrays.asList(topic));
                return consumer;
    }


    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.getLogger(TweetConsumer.class.getName());

        // Initializing Admin SDK
        FileInputStream serviceAccount =
                new FileInputStream("./serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://myreactfirebaseproject-ba5b0.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);

        // Adding db handle
        Firestore db = FirestoreClient.getFirestore();

        logger.info("Connected to FireStore....");
        KafkaConsumer<String, String> consumer = createConsumer("tweets_to_firebase");

        logger.info("Created consumer....");
        Gson gson = new Gson();

        // poll for data
        while (true) {
            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<String, String> record: records){
                logger.info("Key: " + record.key() + " Value: " + record.value() + "\n" +
                        "Partition: " + record.partition() + " Offset: " + record.offset());

                //String id = extractCFNFromStringMessage(record.value());

                Tweet tweetData = gson.fromJson(record.value(), Tweet.class);
                logger.info(tweetData.getId_str());

                // Writing events to Firestore
                db.collection("tweets").document(tweetData.getId_str()).set(tweetData);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String extractCFNFromStringMessage(String jsonString){
        // gson library
        return JsonParser.parseString(jsonString)
                .getAsJsonObject()
                .get("id_str")
                .getAsString();
    }
}
