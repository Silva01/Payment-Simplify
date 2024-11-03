package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    @Transactional
    public void debitingAndCrediting(TransactionRequest request) {
        debit(request);
        credit(request);
    }


    public void debit(TransactionRequest request) {
        final var payerWallet = findById(request.getPayer())
                .orElseThrow(() -> new RuntimeException("Payer not found"));

        walletRepository.save(payerWallet.debit(request.getValue()));
    }

    public void credit(TransactionRequest request) {
        final var payeeWallet = findById(request.getPayee())
                .orElseThrow(() -> new RuntimeException("Payee not found"));

        walletRepository.save(payeeWallet.credit(request.getValue()));
    }
}
