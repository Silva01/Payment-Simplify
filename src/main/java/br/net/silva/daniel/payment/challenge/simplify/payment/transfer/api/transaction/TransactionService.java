package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final WalletService walletService;
    private final List<TransactionValidate> transactionValidates;

    @Transactional
    public void createTransferTransaction(TransactionRequest request) throws TransactionNotAllowsException {
        // 1 - validar a transação
        final var payerWallet = walletService.findById(request.getPayer())
                .orElseThrow(() -> new TransactionNotAllowsException("Payer not found"));

        transactionValidates.forEach(validate -> validate.validate(request, payerWallet));

        // 2 - Debitar o valor da carteira do pagador
        walletService.debitingAndCrediting(request);
        // 4 - Salvar a transação


        // 5 - Enviar notificação para o recebedor
    }
}
