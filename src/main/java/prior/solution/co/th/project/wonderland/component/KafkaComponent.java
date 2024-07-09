package prior.solution.co.th.project.wonderland.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaComponent {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaComponent(@Qualifier("registerKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendData(String message, String topic) {
        this.kafkaTemplate.send(topic, message)
                .whenComplete((result, throwable) -> {
                    if (null != throwable) {
                        log.info("kafka send to {} done {]"
                                , result.getRecordMetadata().topic()
                                , result.getProducerRecord().value());
                    }else {
                        log.info("kafka send exception {}", throwable.getMessage());
                    }
                });
    }
}
