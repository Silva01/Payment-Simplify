package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.enuns.StatusTransfer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transfer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {

    @Id
    @GeneratedValue(generator = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "account_sender", referencedColumnName = "id", updatable = false, insertable = false)
    private Account sender;

    @OneToOne
    @JoinColumn(name = "account_receiver", referencedColumnName = "id", updatable = false, insertable = false)
    private Account receiver;

    @Column(name = "amount")
    private BigDecimal value;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    @Column(name = "idempotency_id")
    private String idempotencyId;

    private StatusTransfer status;

    private String message;
}
