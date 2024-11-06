package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationConsumer {

    private final RestClient restClient;

    public NotificationConsumer(RestClient.Builder restBuilder) {
        this.restClient = restBuilder
                .baseUrl("https://util.devi.tools/api/v1/notify")
                .build();
    }

    @KafkaListener(topics = "simplify.payment.transaction.notification", groupId = "simplify-payment-challenger")
    public void send(Transaction transaction) {

        final var response = restClient.get().retrieve().toEntity(Notification.class);

        if (response.getStatusCode().isError() || response.getBody().status().equals("fail")) {
            throw new NotificationNotSendException("Error to send notification");
        }
    }


}
