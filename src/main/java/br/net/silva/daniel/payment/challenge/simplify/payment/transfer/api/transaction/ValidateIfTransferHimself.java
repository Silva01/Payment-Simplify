package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfTransferHimself implements TransactionValidate {

    @Override
    public void validate(TransactionRequest request, Wallet wallet) throws TransactionNotAllowsException {
        if (wallet.getId().equals(request.getPayee())) {
            throw new TransactionNotAllowsException("Payer cannot transfer to himself");
        }
    }
}
