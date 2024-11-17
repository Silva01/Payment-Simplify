package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationRestClient;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification.NotificationRestClient;
import org.springframework.web.client.RestClient;

public class RestClientClientAdapter implements AuthorizationRestClient, NotificationRestClient {

    private final RestClient restClient;

    public RestClientClientAdapter(RestClient.Builder restClientBuilder, String uri) {
        this.restClient = restClientBuilder
                .baseUrl(uri)
                .build();
    }

    @Override
    public RestClient.ResponseSpec exec() {
        return restClient.get().retrieve();
    }
}
