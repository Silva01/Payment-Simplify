package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ClientResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Account;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.ClientDetails;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public ClientResponse create(ClientRequest request) throws ClientAlreadyExistsException {
        final var newClient = new Client();
        newClient.setCpf(request.getIdentify());

        final var details = new ClientDetails();
        details.setEmail(request.getEmail());
        details.setName(request.getName());

        final var account = new Account();
        account.setType(AccountType.CLIENT);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setPassword(request.getPassword());
        newClient.setAccount(account);

        newClient.setDetails(details);

        repository.save(newClient);
        return new ClientResponse(0L, newClient.getCpf());
    }
}
