package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "WALLETS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Wallet {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private BigDecimal balance;

    @Convert(converter = WalletTypeConverter.class)
    private WalletTypeEnum type;

    public Wallet debit(BigDecimal value) {
        return new Wallet(id, name, cpf, email, password, balance.subtract(value), type);
    }

    public Wallet credit(BigDecimal value) {
        return new Wallet(id, name, cpf, email, password, balance.add(value), type);
    }
}
