package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.AccountModel;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.AccountRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.FakerBuilder.ClientFaker.buildClientRequest;

@Component
@AllArgsConstructor
public class Fixture {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    public AccountModel createCustomAccount(AccountType type) {
        final var request = buildClientRequest();
        final var newCLient = createCustomAccount(request);
        final var newAccount = createCustomAccount(newCLient, request, type);

        newCLient.setAccountModel(newAccount);
        final var responseAccount = accountRepository.save(newAccount);
        clientRepository.save(newCLient);

        return responseAccount;
    }

    public AccountModel createCustomAccount(Client newCLient, ClientRequest request, AccountType type) {
        return AccountModel
                .builder()
                .balance(BigDecimal.valueOf(1000))
                .type(type)
                .clientIdentity(newCLient.getId())
                .password(request.getPassword())
                .build();
    }

    public Client createCustomAccount(ClientRequest request) {
        final var newClient = Client
                .builder()
                .email(request.getEmail())
                .name(request.getName())
                .id(request.getCpf())
                .build();

        return clientRepository.save(newClient);
    }
}
