package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "WALLETS")
public record Wallet(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        String name,
        String cpf,
        String email,
        String password,
        @Enumerated(EnumType.ORDINAL) WalletTypeEnum type
) {
}
