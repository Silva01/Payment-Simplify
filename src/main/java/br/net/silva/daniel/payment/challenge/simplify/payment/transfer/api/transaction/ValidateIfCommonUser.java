package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfCommonUser implements TransactionValidate {

    @Override
    public void validate(TransactionRequest request, Wallet wallet) throws TransactionNotAllowsException {
        if (!WalletTypeEnum.COMUM.getValue().equals(wallet.getType().getValue())) {
            throw new TransactionNotAllowsException("Payer type is not allowed to make transfers");
        }
    }
}
