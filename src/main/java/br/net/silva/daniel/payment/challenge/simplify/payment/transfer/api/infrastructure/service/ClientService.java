package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ClientResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.factory.ClientFactory;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public ClientResponse create(ClientRequest request) throws ClientAlreadyExistsException {
        validateIfAccountExists(request.identify(), request.getEmail());
        final var newClient = repository.save(ClientFactory.createClient(request));
        return new ClientResponse(newClient.getAccount().getId(), newClient.getId());
    }

    public void validateIfAccountExists(String identify, String email) throws ClientAlreadyExistsException {
        final var clientOpt = repository.findByIdOrEmail(identify, email);
        if (clientOpt.isPresent()) {
            throw new ClientAlreadyExistsException(createMessageError(identify, email, clientOpt.get()));
        }
    }

    private String createMessageError(String identify, String email, Client client) {
        if (client.getId().equals(identify))
            return String.format("Invalid Request: Client with CPF/CNPJ %s already exists", identify);
        else
            return String.format("Invalid Request: Client with email %s already exists", email);
    }
}
