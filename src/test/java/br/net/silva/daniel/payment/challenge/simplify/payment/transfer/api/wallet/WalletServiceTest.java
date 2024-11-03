package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.WalletFactoryMock;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionNotAllowsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest implements WalletFactoryMock {

    @InjectMocks
    private WalletService service;

    @Mock
    private WalletRepository repository;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @Test
    void findWalletById_WalletValidInDatabase_ReturnWalletWithSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(createCommonWallet(1L)));

        final var sut = service.findById(1L);

        assertThat(sut).isNotNull();
        assertThat(sut.id()).isEqualTo(1L);
        assertThat(sut.name()).isEqualTo("Teste");
        assertThat(sut.cpf()).isEqualTo("12345678900");
        assertThat(sut.email()).isEqualTo("teste@teste.com");
        assertThat(sut.password()).isEqualTo("123");
        assertThat(sut.balance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(sut.type()).isEqualTo(WalletTypeEnum.COMUM);

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findWalletById_WalletNotExists_ReturnWalletException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(1L))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer not found");

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void debitWallet_WithValidData_ExecuteWithSuccess() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(1L)).thenReturn(Optional.of(createCommonWallet(1L)));

        assertThatCode(() -> service.debit(request))
                .doesNotThrowAnyException();

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(walletCaptor.capture());

        final var wallet = walletCaptor.getValue();

        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.name()).isEqualTo("Teste");
        assertThat(wallet.cpf()).isEqualTo("12345678900");
        assertThat(wallet.email()).isEqualTo("teste@teste.com");
        assertThat(wallet.password()).isEqualTo("123");
        assertThat(wallet.balance()).isEqualTo(BigDecimal.valueOf(900));
    }

    @Test
    void debitWallet_WalletNotExists_ThrowsError() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.debit(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer not found");

        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(Wallet.class));
    }

    @Test
    void creditWallet_WithValidData_ExecuteWithSuccess() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(request.getPayee())).thenReturn(Optional.of(createCommonWallet(2L)));

        assertThatCode(() -> service.credit(request))
                .doesNotThrowAnyException();

        verify(repository, times(1)).findById(request.getPayee());
        verify(repository, times(1)).save(walletCaptor.capture());

        final var wallet = walletCaptor.getValue();

        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(request.getPayee());
        assertThat(wallet.name()).isEqualTo("Teste");
        assertThat(wallet.cpf()).isEqualTo("12345678900");
        assertThat(wallet.email()).isEqualTo("teste@teste.com");
        assertThat(wallet.password()).isEqualTo("123");
        assertThat(wallet.balance()).isEqualTo(BigDecimal.valueOf(1100));
    }

    @Test
    void creditWallet_WalletNotExists_ThrowsError() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(request.getPayee())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.credit(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer not found");

        verify(repository, times(1)).findById(request.getPayee());
        verify(repository, never()).save(any(Wallet.class));
    }

    @Test
    void creditAndDebitWallet_WithValidData_ExecuteWithSuccess() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(createCommonWallet(1L)))
                .thenReturn(Optional.of(createCommonWallet(2L)));

        assertThatCode(() -> service.debitingAndCrediting(request))
                .doesNotThrowAnyException();

        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(2)).save(walletCaptor.capture());

        final var walletValues = walletCaptor.getAllValues();

        assertThat(walletValues).hasSize(2);

        final var walletDebitSut = walletValues.get(0);
        assertThat(walletDebitSut).isNotNull();
        assertThat(walletDebitSut.id()).isEqualTo(request.getPayer());
        assertThat(walletDebitSut.name()).isEqualTo("Teste");
        assertThat(walletDebitSut.cpf()).isEqualTo("12345678900");
        assertThat(walletDebitSut.email()).isEqualTo("teste@teste.com");
        assertThat(walletDebitSut.password()).isEqualTo("123");
        assertThat(walletDebitSut.balance()).isEqualTo(BigDecimal.valueOf(900));

        final var walletCreditSut = walletValues.get(1);
        assertThat(walletCreditSut).isNotNull();
        assertThat(walletCreditSut.id()).isEqualTo(request.getPayee());
        assertThat(walletCreditSut.name()).isEqualTo("Teste");
        assertThat(walletCreditSut.cpf()).isEqualTo("12345678900");
        assertThat(walletCreditSut.email()).isEqualTo("teste@teste.com");
        assertThat(walletCreditSut.password()).isEqualTo("123");
        assertThat(walletCreditSut.balance()).isEqualTo(BigDecimal.valueOf(1100));
    }

    @Test
    void creditAndDebitWallet_WalletCommonNotExists_ThrowsError() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.debitingAndCrediting(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer not found");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, never()).save(any(Wallet.class));
    }

    @Test
    void creditAndDebitWallet_WalletLojistaNotExists_ThrowsError() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(createCommonWallet(1L)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.debitingAndCrediting(request))
                .isInstanceOf(TransactionNotAllowsException.class)
                .hasMessage("Payer not found");

        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).save(walletCaptor.capture());

        final var wallet = walletCaptor.getValue();

        assertThat(wallet).isNotNull();
        assertThat(wallet.id()).isEqualTo(1L);
        assertThat(wallet.name()).isEqualTo("Teste");
        assertThat(wallet.cpf()).isEqualTo("12345678900");
        assertThat(wallet.email()).isEqualTo("teste@teste.com");
        assertThat(wallet.password()).isEqualTo("123");
        assertThat(wallet.balance()).isEqualTo(BigDecimal.valueOf(900));
    }
}