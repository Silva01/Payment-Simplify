package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationKafkaProducer implements NotificationProducer {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    @Override
    public void sendNotification(Transaction transaction) {
        kafkaTemplate.send("simplify.payment.transaction.notification", transaction);
    }
}
