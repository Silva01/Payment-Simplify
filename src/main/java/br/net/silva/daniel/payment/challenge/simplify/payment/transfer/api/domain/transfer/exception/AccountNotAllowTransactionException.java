package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception;

public class AccountNotAllowTransactionException extends Exception {
    public AccountNotAllowTransactionException(String message) {
        super(message);
    }
}
