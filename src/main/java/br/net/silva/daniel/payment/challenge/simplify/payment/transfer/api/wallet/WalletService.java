package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionRequest;

public interface WalletService {
    Wallet findById(Long id);
    void debitingAndCrediting(TransactionRequest request);
    void debit(TransactionRequest request);
    void credit(TransactionRequest request);
}
