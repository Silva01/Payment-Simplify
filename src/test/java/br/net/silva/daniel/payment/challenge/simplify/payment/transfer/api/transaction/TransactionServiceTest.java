package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationService;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletService;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Mock
    private ValidateIfHasBalance validateIfHasBalance;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AuthorizationService authorizationService;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "transactionValidates",
                                     List.of(validateIfCommonUser, validateIfTransferHimself, validateIfHasBalance));
    }

    @Test
    void createTransaction_ToTransferWithValidData_MakeWithSuccess() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(1000));
        final var walletCommom = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", BigDecimal.valueOf(1000), WalletTypeEnum.COMUM);

        when(walletService.findById(request.getPayer())).thenReturn(walletCommom);

        assertThatCode(() -> service.createTransferTransaction(request))
                .doesNotThrowAnyException();

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletCommom);
        verify(validateIfTransferHimself, times(1)).validate(request, walletCommom);
        verify(validateIfHasBalance, times(1)).validate(request, walletCommom);
        verify(walletService, times(1)).debitingAndCrediting(request);
        verify(repository, times(1)).save(transactionCaptor.capture());

        final var transaction = transactionCaptor.getValue();

        assertThat(transaction.getPayer()).isEqualTo(request.getPayer());
        assertThat(transaction.getPayee()).isEqualTo(request.getPayee());
        assertThat(transaction.getValue()).isEqualTo(request.getValue());

    }

    @Test
    void createTransaction_ToTransferLojistaToCommonUser_MakeWithError() {
        final var request = new TransactionRequest(2L, 1L, BigDecimal.valueOf(1000));
        final var walletLojista = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", BigDecimal.valueOf(1000), WalletTypeEnum.LOJISTA);

        when(walletService.findById(request.getPayer())).thenReturn(walletLojista);
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
        final var walletLojista = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", BigDecimal.valueOf(1000), WalletTypeEnum.COMUM);

        when(walletService.findById(request.getPayer())).thenReturn(walletLojista);
        doThrow(new TransactionNotAllowsException("Payer cannot transfer to himself"))
                .when(validateIfTransferHimself).validate(any(TransactionRequest.class), any(Wallet.class));

        assertThatThrownBy(() -> service.createTransferTransaction(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer cannot transfer to himself");

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletLojista);
        verify(validateIfTransferHimself, times(1)).validate(request, walletLojista);
    }

    @Test
    void createTransaction_ToTransferWithoutBalance_MakeWithError() {
        final var request = new TransactionRequest(2L, 1L, BigDecimal.valueOf(1000));
        final var walletCommon = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", BigDecimal.valueOf(1000), WalletTypeEnum.COMUM);

        when(walletService.findById(request.getPayer())).thenReturn(walletCommon);
        doThrow(new TransactionNotAllowsException("Payer does not have enough balance to make the transfer"))
                .when(validateIfHasBalance).validate(any(TransactionRequest.class), any(Wallet.class));

        assertThatThrownBy(() -> service.createTransferTransaction(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer does not have enough balance to make the transfer");

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletCommon);
        verify(validateIfTransferHimself, times(1)).validate(request, walletCommon);
        verify(validateIfHasBalance, times(1)).validate(request, walletCommon);
    }

    @Test
    void createTransaction_ToTransferWithValidData_NotAuthorizated() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(1000));
        final var walletCommom = new Wallet(1L, "Teste", "12345678900", "teste@teste.com", "123", BigDecimal.valueOf(1000), WalletTypeEnum.COMUM);

        when(walletService.findById(request.getPayer())).thenReturn(walletCommom);
        doThrow(new AuthorizationException("Transaction not authorized"))
                .when(authorizationService).authorizateTransaction();

        assertThatThrownBy(() -> service.createTransferTransaction(request))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Transaction not authorized");

        verify(walletService, times(1)).findById(request.getPayer());
        verify(validateIfCommonUser, times(1)).validate(request, walletCommom);
        verify(validateIfTransferHimself, times(1)).validate(request, walletCommom);
        verify(validateIfHasBalance, times(1)).validate(request, walletCommom);
        verify(walletService, times(1)).debitingAndCrediting(request);
        verify(repository, times(1)).save(transactionCaptor.capture());
        verify(authorizationService, times(1)).authorizateTransaction();

        final var transaction = transactionCaptor.getValue();

        assertThat(transaction.getPayer()).isEqualTo(request.getPayer());
        assertThat(transaction.getPayee()).isEqualTo(request.getPayee());
        assertThat(transaction.getValue()).isEqualTo(request.getValue());

    }
}