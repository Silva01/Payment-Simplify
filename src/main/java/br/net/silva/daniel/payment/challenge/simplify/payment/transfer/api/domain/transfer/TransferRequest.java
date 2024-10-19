package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer;

import java.math.BigDecimal;

public record TransferRequest(
        BigDecimal value,
        Integer payer,
        Integer payee
) {
}
