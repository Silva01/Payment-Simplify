package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

public class NotificationNotSendException extends RuntimeException {
    public NotificationNotSendException(String message) {
        super(message);
    }
}
