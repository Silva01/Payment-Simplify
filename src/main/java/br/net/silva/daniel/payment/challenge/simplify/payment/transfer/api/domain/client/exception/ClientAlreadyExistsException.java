package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception;

public class ClientAlreadyExistsException extends Exception {
    public ClientAlreadyExistsException(String message) {
        super(message);
    }
}
