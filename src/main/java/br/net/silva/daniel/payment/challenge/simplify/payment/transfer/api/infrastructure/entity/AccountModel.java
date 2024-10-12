package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountType type;
    private BigDecimal balance;
    private String password;

    @Column(name = "client_identity")
    private String clientIdentity;
}
