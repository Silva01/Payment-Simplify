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

    public void createCustomAccount(Long accountId) {
        final var request = buildClientRequest();
        final var newCLient = createCustomAccount(request);
        final var newAccount = createCUstomAccount(accountId, newCLient, request);

        newCLient.setAccountModel(newAccount);
        accountRepository.save(newAccount);
        clientRepository.save(newCLient);
    }

    public AccountModel createCUstomAccount(Long accountId, Client newCLient, ClientRequest request) {
        final var newAccount = AccountModel
                .builder()
                .id(accountId)
                .balance(BigDecimal.valueOf(1000))
                .type(AccountType.CLIENT)
                .clientIdentity(newCLient.getId())
                .password(request.getPassword())
                .build();
        return newAccount;
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
