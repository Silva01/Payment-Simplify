package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountModel, Long>{
}
