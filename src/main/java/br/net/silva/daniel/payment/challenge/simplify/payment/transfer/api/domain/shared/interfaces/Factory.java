package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.shared.interfaces;

public interface Factory <T, P> {
    T convertToEntityFrom(P p);
}
