package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.model;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class Account {
    private final Long id;
    private final AccountType type;
    private final BigDecimal balance;
    private final String password;
}
