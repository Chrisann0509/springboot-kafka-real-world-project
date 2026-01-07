package net.javaguides.springboot;

import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.EventHandler;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class WikimediaChangesProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(WikimediaChangesProducer.class);
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public WikimediaChangesProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage() throws InterruptedException {
        String topic = topicName;

        // to read real time stream data for wikimedia, we use event handler
        EventHandler eventHandler = new WikimediaChangesHandler(kafkaTemplate, topicName);
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";
        Headers headers = new Headers.Builder()
                .add("User-Agent", "SpringBoot-Kafka-Producer/1.0 (your-email@example.com)")
                .build();
        EventSource eventSource = new EventSource.Builder(
                eventHandler,
                URI.create(url))
                .headers(headers)
                .build();

        eventSource.start();
        LOGGER.info("EventSource started");
        TimeUnit.MINUTES.sleep(10);
    }
}
