package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

public class TransactionNotAllowsException extends RuntimeException {
    public TransactionNotAllowsException(String message) {
        super(message);
    }
}
