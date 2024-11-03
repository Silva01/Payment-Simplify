package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionRequest{
    private final Long payer;
    private final Long payee;
    private final BigDecimal value;
}
