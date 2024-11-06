package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACTIONS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private Long payer;
    private Long payee;
    private BigDecimal value;
    @CreatedDate private LocalDateTime createdAt;

    public static Transaction of(TransactionRequest request) {
        return new Transaction(null, request.getPayer(), request.getPayee(), request.getValue(), null);
    }
}
