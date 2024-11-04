package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

public record Notification(
        String status,
        NotificationData data
) {
}
