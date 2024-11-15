package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class AuthorizationService implements Authorizator {

    private static final String AUTHORIZE_URL = "https://util.devi.tools/api/v2/authorize";
    final RestClient restClient;

    public AuthorizationService(RestClient.Builder restBuilder) {
        this.restClient = restBuilder
                .baseUrl(AUTHORIZE_URL)
                .build();
    }


    @Override
    public void authorizeTransaction() {
        final var response = restClient.get().retrieve().toEntity(Authorization.class);
        final var body = Optional.ofNullable(response.getBody());

        if (body.isEmpty()) {
            throw new AuthorizationException("Error to authorize transaction");
        }

        if (response.getStatusCode().isError() || !body.get().data().authorization()) {
            throw new AuthorizationException("Transaction not authorized");
        }
    }
}
