package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.Transaction;

public interface NotificationProducer {
    void sendNotification(Transaction transaction);
}
