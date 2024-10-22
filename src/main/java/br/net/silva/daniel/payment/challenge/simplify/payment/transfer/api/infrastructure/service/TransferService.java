package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Validate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.Account;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.annotation.TransferAccountValidate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransferService {

    @TransferAccountValidate
    private final List<Validate<Account>> transferAccountValidates;

    public void transferValue(TransferRequest request) throws BadTransferException {

        for (Validate<Account> validate : transferAccountValidates) {
            validate.validate(Account.accountNotFound());
        }

        CommandInvoker.of().add(() -> System.out.println("Opa")).start();
    }
}
