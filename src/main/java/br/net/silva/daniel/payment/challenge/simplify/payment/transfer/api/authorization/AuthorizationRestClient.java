package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

import org.springframework.web.client.RestClient;

public interface AuthorizationRestClient {
    RestClient.ResponseSpec exec();
}
