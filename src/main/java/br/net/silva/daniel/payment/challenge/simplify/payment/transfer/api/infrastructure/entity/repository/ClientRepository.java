package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String>{
}
