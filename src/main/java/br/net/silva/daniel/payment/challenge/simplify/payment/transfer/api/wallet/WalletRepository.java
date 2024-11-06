package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}
