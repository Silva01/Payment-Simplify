package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Validate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.Account;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService service;

    @Mock
    private List<Validate<Account>> transferAccountValidates;

    @Mock
    private Validate<Account> validateMock;

    @BeforeEach
    void setUp() {
        final var validateListMock = List.of(validateMock);
        when(transferAccountValidates.iterator()).thenReturn(validateListMock.iterator());
    }

    @Test
    void createTransfer_WithValidData_ExecuteWithoutError() {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(100), 1, 2);
        // Act

        assertThatCode(() -> service.transferValue(request))
                .doesNotThrowAnyException();
    }

    @Test
    void createTransfer_WithInvalidData_ThrowBadTransferException() throws BadTransferException {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(100), 1, 2);

        doThrow(new BadTransferException("Account Not Found", 404)).when(validateMock).validate(any(Account.class));

        // Act/Assert
        assertThatThrownBy(() -> service.transferValue(request))
                .isInstanceOf(BadTransferException.class)
                .hasMessage("Account Not Found");
    }
}