package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.Fixture;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.component.TransferRequestToEntityFactory;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Transfer;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.AccountRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.FakerBuilder.TransferFaker.buildTransferRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    private TransferService service;

    @Autowired
    private TransferRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Fixture fixture;

    @BeforeEach
    void setUp() {
        service = new TransferService(repository, new TransferRequestToEntityFactory(accountRepository));
        fixture = new Fixture(clientRepository, accountRepository);
    }

    @Test
    void transferValue_WithValidData_TransferWithSuccess() throws Exception {
        final var request = buildTransferRequest();
        fixture.createCustomAccount(request.payer());
        fixture.createCustomAccount(request.payee());

        final var sut = service.transfer(request);
        assertThat(sut).isNotNull();
        assertThat(sut.transactionId()).isNotNull();

        final var transferSut = entityManager.find(Transfer.class, UUID.fromString(sut.transactionId()));
        assertThat(transferSut).isNotNull();
        assertThat(transferSut.getValue()).isEqualTo(request.value());
        assertThat(transferSut.getSender().getId()).isEqualTo(request.payer());
        assertThat(transferSut.getReceiver().getId()).isEqualTo(request.payee());
    }
}