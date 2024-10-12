package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountUnauthorizedTransactionException;

public interface Authorizer {
    void authorize() throws AccountUnauthorizedTransactionException;
}
