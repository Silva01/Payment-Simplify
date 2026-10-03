package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
  Optional<Wallet> findByCpf(String cpf);

  Optional<Wallet> findByEmail(String email);
}
