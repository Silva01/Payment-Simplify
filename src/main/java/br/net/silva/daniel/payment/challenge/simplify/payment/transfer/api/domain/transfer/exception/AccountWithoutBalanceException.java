package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception;

public class AccountWithoutBalanceException extends Exception {
    public AccountWithoutBalanceException(String message) {
        super(message);
    }
}
