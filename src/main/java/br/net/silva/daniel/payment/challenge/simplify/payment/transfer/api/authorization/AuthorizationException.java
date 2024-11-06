package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}
