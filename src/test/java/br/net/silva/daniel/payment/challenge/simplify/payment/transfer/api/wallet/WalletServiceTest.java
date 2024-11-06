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
        assertThat(sut.getId()).isEqualTo(1L);
        assertThat(sut.getName()).isEqualTo("Teste");
        assertThat(sut.getCpf()).isEqualTo("12345678900");
        assertThat(sut.getEmail()).isEqualTo("teste@teste.com");
        assertThat(sut.getPassword()).isEqualTo("123");
        assertThat(sut.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(sut.getType()).isEqualTo(WalletTypeEnum.COMUM);

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
        assertThat(wallet.getId()).isEqualTo(1L);
        assertThat(wallet.getName()).isEqualTo("Teste");
        assertThat(wallet.getCpf()).isEqualTo("12345678900");
        assertThat(wallet.getEmail()).isEqualTo("teste@teste.com");
        assertThat(wallet.getPassword()).isEqualTo("123");
        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.valueOf(900));
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
        assertThat(wallet.getId()).isEqualTo(request.getPayee());
        assertThat(wallet.getName()).isEqualTo("Teste");
        assertThat(wallet.getCpf()).isEqualTo("12345678900");
        assertThat(wallet.getEmail()).isEqualTo("teste@teste.com");
        assertThat(wallet.getPassword()).isEqualTo("123");
        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.valueOf(1100));
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
        assertThat(walletDebitSut.getId()).isEqualTo(request.getPayer());
        assertThat(walletDebitSut.getName()).isEqualTo("Teste");
        assertThat(walletDebitSut.getCpf()).isEqualTo("12345678900");
        assertThat(walletDebitSut.getEmail()).isEqualTo("teste@teste.com");
        assertThat(walletDebitSut.getPassword()).isEqualTo("123");
        assertThat(walletDebitSut.getBalance()).isEqualTo(BigDecimal.valueOf(900));

        final var walletCreditSut = walletValues.get(1);
        assertThat(walletCreditSut).isNotNull();
        assertThat(walletCreditSut.getId()).isEqualTo(request.getPayee());
        assertThat(walletCreditSut.getName()).isEqualTo("Teste");
        assertThat(walletCreditSut.getCpf()).isEqualTo("12345678900");
        assertThat(walletCreditSut.getEmail()).isEqualTo("teste@teste.com");
        assertThat(walletCreditSut.getPassword()).isEqualTo("123");
        assertThat(walletCreditSut.getBalance()).isEqualTo(BigDecimal.valueOf(1100));
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
        assertThat(wallet.getId()).isEqualTo(1L);
        assertThat(wallet.getName()).isEqualTo("Teste");
        assertThat(wallet.getCpf()).isEqualTo("12345678900");
        assertThat(wallet.getEmail()).isEqualTo("teste@teste.com");
        assertThat(wallet.getPassword()).isEqualTo("123");
        assertThat(wallet.getBalance()).isEqualTo(BigDecimal.valueOf(900));
    }
}