package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationConsumer {

    final NotificationRestClient notificationRestClient;

    public NotificationConsumer(NotificationRestClient notificationRestClient) {
        this.notificationRestClient = notificationRestClient;
    }

    @KafkaListener(topics = "simplify.payment.transaction.notification", groupId = "simplify-payment-challenger")
    public void send(Transaction transaction) {

        final var response = notificationRestClient.exec().toEntity(Notification.class);
        final var body = Optional.ofNullable(response.getBody());

        if (body.isEmpty()) {
            throw new NotificationNotSendException("Error to send notification");
        }

        if (response.getStatusCode().isError() || body.get().status().equals("fail")) {
            throw new NotificationNotSendException("Error to send notification");
        }
    }


}
