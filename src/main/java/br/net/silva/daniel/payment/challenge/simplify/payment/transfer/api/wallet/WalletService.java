package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WalletService {

    public Optional<Wallet> findById(Long id) {
        return Optional.empty();
    }
}
