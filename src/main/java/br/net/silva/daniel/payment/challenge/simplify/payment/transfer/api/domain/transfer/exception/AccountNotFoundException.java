package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
