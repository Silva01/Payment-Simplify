package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfHasBalance implements TransactionValidate {

    @Override
    public void validate(TransactionRequest request, Wallet wallet) throws TransactionNotAllowsException {
        if (wallet.getBalance().compareTo(request.getValue()) < 0) {
            throw new TransactionNotAllowsException("Payer does not have enough balance to make the transfer");
        }
    }
}
