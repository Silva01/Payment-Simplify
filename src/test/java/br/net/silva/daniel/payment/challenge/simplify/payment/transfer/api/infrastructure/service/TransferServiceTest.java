package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        final var accountPayer = fixture.createCustomAccount();
        final var accountPayee = fixture.createCustomAccount();

        final var request = buildTransferRequest(accountPayer.getId(), accountPayee.getId());

        final var sut = service.transfer(request);
        assertThat(sut).isNotNull();
        assertThat(sut.transactionId()).isNotNull();

        final var transferSut = entityManager.find(Transfer.class, UUID.fromString(sut.transactionId()));
        assertThat(transferSut).isNotNull();
        assertThat(transferSut.getValue()).isEqualTo(request.value());
        assertThat(transferSut.getSender().getId()).isEqualTo(request.payer());
        assertThat(transferSut.getReceiver().getId()).isEqualTo(request.payee());
    }

    @Test
    void transferValue_WithAccountPayerNotExists_ReturnsAccountNotFoundException() {
        final var request = buildTransferRequest(200L, 201L);

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(String.format("Account with id %d not found", request.payer()));
    }

    @Test
    void transferValue_WithAccountPayeeNotExists_ReturnsAccountNotFoundException() {
        final var accountPayer = fixture.createCustomAccount();
        final var request = buildTransferRequest(accountPayer.getId(), 201L);

        assertThatThrownBy(() -> service.transfer(request))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessage(String.format("Account with id %d not found", request.payee()));
    }
}