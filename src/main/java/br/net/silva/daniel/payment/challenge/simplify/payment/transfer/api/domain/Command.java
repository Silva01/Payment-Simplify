package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

public interface Command <T, E extends Throwable> {
    void execute(T t) throws E;
}
