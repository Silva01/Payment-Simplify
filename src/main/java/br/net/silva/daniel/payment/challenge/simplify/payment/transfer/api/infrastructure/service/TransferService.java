package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.TransferResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferService {
    public TransferResponse transfer(TransferRequest request) throws Exception {
        return new TransferResponse(UUID.randomUUID().toString());
    }
}
