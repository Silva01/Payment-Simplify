package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception;

public class AccountUnauthorizedTransactionException extends Exception {
    public AccountUnauthorizedTransactionException(String message) {
        super(message);
    }
}
