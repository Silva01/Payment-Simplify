package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.shared.interfaces.Factory;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequestTransaction;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.TransferResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Transfer;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferService {

    private final TransferRepository repository;
    private final Factory<Transfer, TransferRequestTransaction> factory;

    public TransferResponse transfer(TransferRequestTransaction request) throws Exception {
        final var transfer = factory.convertToEntityFrom(request);
        final var transferSaved = repository.save(transfer);

        return new TransferResponse(transferSaved.getId().toString());
    }
}
