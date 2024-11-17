package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService implements Authorizator {

    final AuthorizationRestClient authorizationRestClient;

    public AuthorizationService(@AuthorizationRestClientConfig AuthorizationRestClient authorizationRestClient) {
        this.authorizationRestClient = authorizationRestClient;
    }


    @Override
    public void authorizeTransaction() {
        final var response = authorizationRestClient.exec().toEntity(Authorization.class);
        final var body = Optional.ofNullable(response.getBody());

        if (body.isEmpty()) {
            throw new AuthorizationException("Error to authorize transaction");
        }

        if (response.getStatusCode().isError() || !body.get().data().authorization()) {
            throw new AuthorizationException("Transaction not authorized");
        }
    }
}
