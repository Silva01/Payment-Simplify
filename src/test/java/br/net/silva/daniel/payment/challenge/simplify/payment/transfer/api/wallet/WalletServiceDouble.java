package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.WalletFactoryMock;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionRequest;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
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

    public WalletServiceDouble() {
        super(mock(WalletRepository.class));
        this.walletCaptor = ArgumentCaptor.captor();
    }

    public void verifyFindByIdProcess(Long id, int time) {
        verify(walletRepository, times(time)).findById(id);
    }

    public void verifySaveDebitProcess() {
        verify(walletRepository, times(2)).save(walletCaptor.capture());

        final var walletCommonUser = walletCaptor.getAllValues().get(0);

        assertThat(walletCommonUser.getId()).isNotNull();
        assertThat(walletCommonUser.getName()).isEqualTo("Teste");
        assertThat(walletCommonUser.getCpf()).isEqualTo("12345678900");
        assertThat(walletCommonUser.getEmail()).isEqualTo("teste@teste.com");
        assertThat(walletCommonUser.getPassword()).isEqualTo("123");
        assertThat(walletCommonUser.getBalance()).isEqualTo(BigDecimal.valueOf(800));

        final var walletLojista = walletCaptor.getAllValues().get(1);

        assertThat(walletLojista.getId()).isNotNull();
        assertThat(walletLojista.getName()).isEqualTo("Teste");
        assertThat(walletLojista.getCpf()).isEqualTo("12345678900");
        assertThat(walletLojista.getEmail()).isEqualTo("teste@teste.com");
        assertThat(walletLojista.getPassword()).isEqualTo("123");
        assertThat(walletLojista.getBalance()).isEqualTo(BigDecimal.valueOf(1200));
    }

    @Override
    public Wallet findById(Long id) {
        when(walletRepository.findById(id)).thenReturn(Optional.of(createCommonWallet(id)));
        return super.findById(id);
    }

    @Override
    public void debit(TransactionRequest request) {
        when(walletRepository.findById(request.getPayer())).thenReturn(Optional.of(createCommonWallet(request.getPayer())));
        super.debit(request);
    }

    @Override
    public void credit(TransactionRequest request) {
        when(walletRepository.findById(request.getPayee())).thenReturn(Optional.of(createCommonWallet(request.getPayee())));
        super.credit(request);
    }
}
