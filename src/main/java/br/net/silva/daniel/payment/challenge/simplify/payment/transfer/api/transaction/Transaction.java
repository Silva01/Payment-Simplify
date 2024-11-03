package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "TRANSACTIONS")
public record Transaction(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        Long payer,
        Long payee,
        BigDecimal value,
        @CreatedDate LocalDateTime createdAt
) {

    public static Transaction of(TransactionRequest request) {
        return new Transaction(null, request.getPayer(), request.getPayee(), request.getValue(), null);
    }
}
