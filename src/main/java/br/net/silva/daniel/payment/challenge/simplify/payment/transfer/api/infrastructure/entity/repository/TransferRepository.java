package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, String> {
}
