package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.UserMockGenerator;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountNotExistsValidateTest implements UserMockGenerator {

    @Test
    void validateIfAccountExists_AccountExists_NotThrowsErros() {
        // arrange
        final var account = createSimpleAccount();
        final var validate = new AccountNotExistsValidate();

        // act/assert
        assertThatCode(() -> validate.validate(account)).doesNotThrowAnyException();
    }

    @Test
    void validateIfAccountExists_AccountNotExists_ThrowsException() {
        // arrange
        final var account = Account.accountNotFound();
        final var validate = new AccountNotExistsValidate();

        // act/assert
        assertThatThrownBy(() -> validate.validate(account))
                .isInstanceOf(BadTransferException.class)
                .hasMessage("Account not found");
    }
}