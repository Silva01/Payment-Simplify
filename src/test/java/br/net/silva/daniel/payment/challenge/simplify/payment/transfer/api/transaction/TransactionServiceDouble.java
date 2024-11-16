package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.Authorizator;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification.NotificationProducer;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletServiceDouble;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceDouble extends TransactionService {

    private final ArgumentCaptor<Transaction> transactionCaptor;

    public TransactionServiceDouble(WalletServiceDouble walletServiceDouble) {
        super(
                walletServiceDouble,
                mock(TransactionRepository.class),
                mock(Authorizator.class),
                mock(NotificationProducer.class),
                List.of(new ValidateIfCommonUser(), new ValidateIfHasBalance(), new ValidateIfTransferHimself())
        );

        this.transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
    }

    @Override
    public void createTransferTransaction(TransactionRequest request) throws TransactionNotAllowsException {
        when(repository.save(any(Transaction.class))).thenReturn(new Transaction(1L, request.getPayer(), request.getPayee(), request.getValue(), LocalDateTime.now()));
        super.createTransferTransaction(request);
    }

    public void verifyFindByIdProcessWithSuccess(TransactionRequest request) {
        ((WalletServiceDouble) walletServiceImpl).verifyFindByIdProcess(request.getPayer(), 2);
        ((WalletServiceDouble) walletServiceImpl).verifyFindByIdProcess(request.getPayee(), 1);
        ((WalletServiceDouble) walletServiceImpl).verifySaveDebitProcess();

        verify(repository, times(1)).save(any(Transaction.class));
        verify(producer, times(1)).sendNotification(transactionCaptor.capture());

        final var transaction = transactionCaptor.getValue();
        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getPayer()).isEqualTo(1L);
        assertThat(transaction.getPayee()).isEqualTo(2L);
        assertThat(transaction.getValue()).isEqualTo(BigDecimal.valueOf(200));
    }
}
