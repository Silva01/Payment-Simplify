package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public void transferValue(TransferRequest request) throws BadTransferException {
        CommandInvoker.of().add(() -> System.out.println("Opa")).start();
    }
}
