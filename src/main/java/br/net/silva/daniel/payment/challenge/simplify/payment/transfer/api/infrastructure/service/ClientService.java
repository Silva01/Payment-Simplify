package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ClientResponse;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    public ClientResponse create(ClientRequest request) throws ClientAlreadyExistsException {
        return null;
    }
}
