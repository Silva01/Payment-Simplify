package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.component;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.shared.interfaces.Factory;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotFoundException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequestTransaction;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Transfer;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class TransferRequestToEntityFactory implements Factory<Transfer, TransferRequestTransaction> {

    private final AccountRepository accountRepository;

    @Override
    public Transfer convertToEntityFrom(TransferRequestTransaction transferRequest) throws AccountNotFoundException {
        return Transfer
                .builder()
                .id(UUID.randomUUID())
                .value(transferRequest.value())
                .sender(accountRepository.findById(transferRequest.payer()).orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %d not found", transferRequest.payer()))))
                .receiver(accountRepository.findById(transferRequest.payee()).orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %d not found", transferRequest.payee()))))
                .build();
    }
}
