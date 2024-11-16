package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletServiceDouble;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;

class TransactionUseCaseTest {

    private TransactionServiceDouble transactionService;

    @BeforeEach
    void setUp() {
        final var walletDouble = new WalletServiceDouble();
        transactionService = new TransactionServiceDouble(walletDouble);
    }

    @Test
    void sendTransaction_TransactionValid_ProcessWithSuccess() {
        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(200));

        assertThatCode(() -> transactionService.createTransferTransaction(request))
                .doesNotThrowAnyException();

        transactionService.verifyFindByIdProcessWithSuccess(request);
    }
}
