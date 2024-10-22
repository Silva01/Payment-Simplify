package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Validate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

public class AccountNotExistsValidate implements Validate<Account> {

    @Override
    public void validate(Account account) throws BadTransferException {
        account.exists();
    }
}
