package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotAllowTransactionException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.validations.ValidationTransaction;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.FakerBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TransactionTest {

    @Mock
    private ValidationTransaction validation;

    @Test
    void createATransactionToTransferFromRequest_WithValidData_ReturnsNewTransaction() {
        final var request = FakerBuilder.TransferFaker.buildTransferRequest(1L, 2L);
        final var transaction = TransactionBuilder.buildTransferTransaction(request, List.of(validation));

        assertThat(transaction).isNotNull();
        assertThatCode(transaction::validate)
                .doesNotThrowAnyException();
    }

    @Test
    void createATransactionToTransferFromRequest_WithSourceAccountIsTypeStore_ReturnException() throws Exception {
        doThrow(new AccountNotAllowTransactionException("Account is not permitted to make transactions"))
                .when(validation).validate(any());

        final var request = FakerBuilder.TransferFaker.buildTransferRequest(1L, 2L);
        final var transaction = TransactionBuilder.buildTransferTransaction(request, List.of(validation));

        assertThat(transaction).isNotNull();

        assertThatCode(transaction::validate)
                .isInstanceOf(AccountNotAllowTransactionException.class);
    }
}