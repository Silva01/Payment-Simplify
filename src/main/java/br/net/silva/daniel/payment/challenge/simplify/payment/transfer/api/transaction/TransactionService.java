package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.Authorizator;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification.NotificationProducer;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    final WalletService walletServiceImpl;
    final TransactionRepository repository;
    final Authorizator authorizator;
    final NotificationProducer producer;
    final List<TransactionValidate> transactionValidates;

    @Transactional
    public void createTransferTransaction(TransactionRequest request) throws TransactionNotAllowsException {
        // 1 - validar a transação
        final var payerWallet = walletServiceImpl.findById(request.getPayer());
        transactionValidates.forEach(validate -> validate.validate(request, payerWallet));

        // 2 - Debitar o valor da carteira do pagador
        walletServiceImpl.debitingAndCrediting(request);

        // 4 - Salvar a transação
        final var newTransaction = repository.save(Transaction.of(request));

        // 5 - Acessar servico autorizador
        authorizator.authorizeTransaction();

        // 6 - Enviar notificação para o recebedor
        producer.sendNotification(newTransaction);
    }
}
