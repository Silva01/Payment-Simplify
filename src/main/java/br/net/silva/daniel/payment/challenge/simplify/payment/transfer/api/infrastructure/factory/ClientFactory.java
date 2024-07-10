package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.factory;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import lombok.NoArgsConstructor;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.factory.AccountFactory.createAccount;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class ClientFactory {

    public static Client createClient(ClientRequest request) {
        return Client
                .builder()
                .id(request.getIdentify())
                .email(request.getEmail())
                .name(request.getName())
                .account(createAccount(request.getPassword(), request.getIdentify(), AccountType.CLIENT))
                .build();
    }
}
