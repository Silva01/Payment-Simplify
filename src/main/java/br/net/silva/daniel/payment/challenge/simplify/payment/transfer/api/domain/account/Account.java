package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Aggregate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.TypeAccountEnum.NOT_FOUND_ACCOUNT;
import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.TypeAccountEnum.RETAILER_ACCOUNT;
import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.TypeAccountEnum.SIMPLE_ACCOUNT;
import static java.util.Objects.isNull;

@ToString
@AllArgsConstructor
public class Account implements Aggregate {

    private final String cpf;
    private final String name;
    private final String email;
    private final String password;
    private BigDecimal balance;
    private TypeAccountEnum type;

    public static Account createSimpleAccount(String cpf, String name, String email, String password) {
        return new Account(cpf, name, email, password, BigDecimal.valueOf(1000), SIMPLE_ACCOUNT);
    }

    public static Account createRetailerAccount(String cpf, String name, String email, String password) {
        return new Account(cpf, name, email, password, BigDecimal.valueOf(1000), RETAILER_ACCOUNT);
    }

    public static Account accountNotFound() {
        return new Account(null, null, null, null, BigDecimal.ZERO, NOT_FOUND_ACCOUNT);
    }

    public void validateAccountBalance(BigDecimal value) throws BadTransferException {
        if (balance.compareTo(value) < 0) {
            throw new BadTransferException("Balance insufficient for transfer", HttpStatus.FORBIDDEN.value());
        }
    }

    public boolean isRetailer() {
        return RETAILER_ACCOUNT.equals(type);
    }

    public void exists() throws BadTransferException {
        if (isNull(type) || NOT_FOUND_ACCOUNT.equals(type)) {
            throw new BadTransferException("Account not found", HttpStatus.NOT_FOUND.value());
        }
    }
}
