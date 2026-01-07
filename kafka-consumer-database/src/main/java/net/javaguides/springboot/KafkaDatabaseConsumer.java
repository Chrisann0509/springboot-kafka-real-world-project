package net.javaguides.springboot;

import net.javaguides.springboot.entity.Wikimedia;
import net.javaguides.springboot.repository.WikimediaDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaDatabaseConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDatabaseConsumer.class);
    private WikimediaDataRepository wikimediaDataRepository;

    public KafkaDatabaseConsumer(WikimediaDataRepository wikimediaDataRepository) {
        this.wikimediaDataRepository = wikimediaDataRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumer(String eventMessage) {
        LOGGER.info(String.format("Event message received -> %s", eventMessage));

        Wikimedia wikimedia = new Wikimedia();
        wikimedia.setWikiEventData(eventMessage);
        wikimediaDataRepository.save(wikimedia);
    }
}
