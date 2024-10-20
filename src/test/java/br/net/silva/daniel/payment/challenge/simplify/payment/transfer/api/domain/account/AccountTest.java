package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.UserMockGenerator;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest implements UserMockGenerator {

    @Test
    @DisplayName("Teste deve criar uma conta simples com sucesso")
    void createNewAccount_SimpleAccount_AccountCreatedWithSuccess() {
        final var account = createStaticSimpleAccount();

        assertThat(account).isNotNull();
        assertThat(account).isInstanceOf(Account.class);
        assertThat(account).hasToString("Account(cpf=12345600099, name=Daniel, email=teste@teste, password=123, balance=1000, type=SIMPLE_ACCOUNT)");
    }

    @Test
    @DisplayName("Teste deve criar uma conta de lojista com sucesso")
    void createNewAccount_RetailerAccount_AccountCreatedWithSuccess() {
        final var account = createStaticRetailerAccount();

        assertThat(account).isNotNull();
        assertThat(account).isInstanceOf(Account.class);
        assertThat(account).hasToString("Account(cpf=12345600099, name=Daniel, email=teste@teste, password=123, balance=1000, type=RETAILER_ACCOUNT)");
    }

    @Test
    void validateAccountBalance_ValueLessThanBalance_ReturnSuccess() {
        final var simpleAccount = createSimpleAccount();
        final var retailerAccount = createRetailerAccount();

        final var valueForTransfer = BigDecimal.valueOf(500);

        assertThatCode(() -> simpleAccount.validateAccountBalance(valueForTransfer))
                .doesNotThrowAnyException();

        assertThatCode(() -> retailerAccount.validateAccountBalance(valueForTransfer))
                .doesNotThrowAnyException();
    }

    @Test
    void validateAccountBalance_ValueMoreThanBalance_ReturnError() {
        final var simpleAccount = createSimpleAccount();
        final var retailerAccount = createRetailerAccount();

        final var valueForTransfer = BigDecimal.valueOf(2000);

        assertThatThrownBy(() -> simpleAccount.validateAccountBalance(valueForTransfer))
                .isInstanceOf(BadTransferException.class)
                .hasMessage("Balance insufficient for transfer");

        assertThatThrownBy(() -> retailerAccount.validateAccountBalance(valueForTransfer))
                .isInstanceOf(BadTransferException.class)
                .hasMessage("Balance insufficient for transfer");
    }

    @Test
    void validateAccountType_AccountIsSimpleUser_ReturnsFalse() {
        final var simpleAccount = createSimpleAccount();

        assertThat(simpleAccount.isRetailer()).isFalse();
    }

    @Test
    void validateAccountType_AccountIsRetailerAccount_ReturnsTrue() {
        final var simpleAccount = createRetailerAccount();

        assertThat(simpleAccount.isRetailer()).isTrue();
    }
}