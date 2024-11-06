package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
