package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import org.springframework.web.client.RestClient;

public interface NotificationRestClient {
    RestClient.ResponseSpec exec();
}
