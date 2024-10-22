package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

public interface Validate <T> {
    void validate(T t) throws BadTransferException;
}
