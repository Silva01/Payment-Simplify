package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.DummyInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService service;

    @Test
    void createTransfer_WithValidData_ExecuteWithoutError() {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(100), 1, 2);
        // Act

        assertThatCode(() -> service.transferValue(request))
                .doesNotThrowAnyException();
    }

    @Test
    void createTransfer_WithInvalidData_ThrowBadTransferException() {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(100), 1, 2);

        try (MockedStatic<CommandInvoker> invokerMockedStatic = mockStatic(CommandInvoker.class)){
            invokerMockedStatic.when(CommandInvoker::of)
                    .thenReturn(new DummyInvoker());

            // Act
            assertThatThrownBy(() -> service.transferValue(request))
                    .isInstanceOf(BadTransferException.class)
                    .hasMessage("Invalid transfer");
        }
    }
}