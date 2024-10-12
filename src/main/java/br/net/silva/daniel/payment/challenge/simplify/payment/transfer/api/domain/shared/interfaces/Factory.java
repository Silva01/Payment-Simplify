package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.shared.interfaces;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotFoundException;

public interface Factory <T, P> {
    T convertToEntityFrom(P p) throws AccountNotFoundException;
}
