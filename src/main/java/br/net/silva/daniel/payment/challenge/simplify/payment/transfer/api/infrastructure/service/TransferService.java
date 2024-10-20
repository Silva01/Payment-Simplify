package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import org.springframework.stereotype.Service;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandResponseFluxEnum.TRANSFER;

@Service
public class TransferService {

    public void transferValue(TransferRequest request) throws BadTransferException {
        CommandInvoker.of().add(() -> new CommandResponse("Hello Word", TRANSFER)).start();
    }
}
