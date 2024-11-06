package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;

public interface TransactionValidate {
    void validate(TransactionRequest request, Wallet wallet) throws TransactionNotAllowsException;
}
