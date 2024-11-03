package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletService;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService service;

    @Mock
    private WalletService walletService;

    @Mock
    private List<TransactionValidate> transactionValidates;

    @Mock
    private ValidateIfCommonUser validateIfCommonUser;

    @Mock
    private ValidateIfTransferHimself validateIfTransferHimself;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "transactionValidates", List.of(validateIfCommonUser, validateIfTransferHimself));
    }

    @Test
    void createTransaction_ToTransferWithValidData_MakeWithSuccess() {
        final var transaction = new TransactionRequest(1L, 2L, BigDecimal.valueOf(1000));

        assertThatCode(() -> service.createTransferTransaction(transaction))
                .doesNotThrowAnyException();
    }

    @Test
    void createTransaction_ToTransferLojistaToCommonUser_MakeWithError() {
        final var request = new TransactionRequest(2L, 1L, BigDecimal.valueOf(1000));
        final var walletLojista = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", WalletTypeEnum.LOJISTA);

        when(walletService.findById(request.getPayer())).thenReturn(Optional.of(walletLojista));
        doThrow(new TransactionNotAllowsException("Payer type is not allowed to make transfers"))
                .when(validateIfCommonUser).validate(any(TransactionRequest.class), any(Wallet.class));

        assertThatThrownBy(() -> service.createTransferTransaction(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer type is not allowed to make transfers");

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletLojista);
    }

    @Test
    void createTransaction_ToTransferToHimself_MakeWithError() {
        final var request = new TransactionRequest(2L, 1L, BigDecimal.valueOf(1000));
        final var walletLojista = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", WalletTypeEnum.LOJISTA);

        when(walletService.findById(request.getPayer())).thenReturn(Optional.of(walletLojista));
        doThrow(new TransactionNotAllowsException("Payer cannot transfer to himself"))
                .when(validateIfTransferHimself).validate(any(TransactionRequest.class), any(Wallet.class));

        assertThatThrownBy(() -> service.createTransferTransaction(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer cannot transfer to himself");

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletLojista);
        verify(validateIfTransferHimself, times(1)).validate(request, walletLojista);
    }
}