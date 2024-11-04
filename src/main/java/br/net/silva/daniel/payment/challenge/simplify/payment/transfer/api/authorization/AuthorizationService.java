package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AuthorizationService {

    public final RestClient restClient;

    public AuthorizationService(RestClient.Builder restBuilder) {
        this.restClient = restBuilder
                .baseUrl("https://util.devi.tools/api/v2/authorize")
                .build();
    }


    public void authorizateTransaction() {
        final var response = restClient.get().retrieve().toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().data().authorization()) {
            throw new AuthorizationException("Transaction not authorized");
        }
    }
}
