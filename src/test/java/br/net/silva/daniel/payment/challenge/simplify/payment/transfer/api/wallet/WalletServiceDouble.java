package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.WalletFactoryMock;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionRequest;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Exemplo de Teste com Dublê de Teste para Use Case
 *
 * Uso parece interessante quando precisamos testar componentes integrados
 */
public class WalletServiceDouble extends WalletServiceImpl implements WalletFactoryMock {

    private final ArgumentCaptor<Wallet> walletCaptor;
    private final WalletRepository repository;

    public WalletServiceDouble() {
        super(mock(WalletRepository.class));
        this.walletCaptor = ArgumentCaptor.captor();
        repository = ((WalletRepository) ReflectionTestUtils.getField(this, "walletRepository"));
    }

    public void verifyFindByIdProcess(Long id) {
        verify(repository, times(1)).findById(id);
    }

    public void verifyDebitProcess(TransactionRequest request) {
        verify(repository, times(1)).findById(request.getPayer());
        verify(repository, times(1)).save(walletCaptor.capture());

        final var wallet = walletCaptor.getValue();

        assertThat(wallet.getId()).isNotNull();
        assertThat(wallet.getName()).isEqualTo("Teste");
        assertThat(wallet.getCpf()).isEqualTo("12345678900");
        assertThat(wallet.getEmail()).isEqualTo("teste@teste.com");
        assertThat(wallet.getPassword()).isEqualTo("123");
        assertThat(wallet.getBalance()).isEqualTo(1800);
    }

    @Override
    public Wallet findById(Long id) {
        when(repository.findById(id)).thenReturn(Optional.of(createCommonWallet(1L)));
        return super.findById(id);
    }

    @Override
    public void debit(TransactionRequest request) {
        when(repository.findById(request.getPayer())).thenReturn(Optional.of(createCommonWallet(request.getPayer())));
        super.debit(request);
    }

    @Override
    public void credit(TransactionRequest request) {
        when(repository.findById(request.getPayee())).thenReturn(Optional.of(createCommonWallet(request.getPayee())));
        super.credit(request);
    }
}
