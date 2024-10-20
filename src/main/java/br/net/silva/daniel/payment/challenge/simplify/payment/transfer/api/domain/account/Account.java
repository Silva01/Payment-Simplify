package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Aggregate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.TypeAccountEnum.RETAILER_ACCOUNT;
import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.TypeAccountEnum.SIMPLE_ACCOUNT;

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

    public void validateAccountBalance(BigDecimal value) throws BadTransferException {
        if (balance.compareTo(value) < 0) {
            throw new BadTransferException("Balance insufficient for transfer", HttpStatus.FORBIDDEN.value());
        }
    }
}
