package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.factory;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Account;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AccountFactory {

    public static Account createAccount(String password, String identifyClient, AccountType type) {
        return Account
                .builder()
                .balance(BigDecimal.valueOf(1000))
                .clientIdentity(identifyClient)
                .password(password)
                .type(type)
                .build();
    }
}
