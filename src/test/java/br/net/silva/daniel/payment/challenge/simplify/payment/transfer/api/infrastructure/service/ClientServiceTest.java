package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.ClientFakerBuilder.buildClientRequest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClientServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private ClientService service;

    @BeforeEach
    void setUp() {
        service = new ClientService(clientRepository);
    }

    @Test
    void createClient_WithValidData_CreateWithSuccess() throws ClientAlreadyExistsException {
        final var request = buildClientRequest();

        final var response = service.create(request);
        assertThat(response).isNotNull();
        assertThat(response.accountId()).isNotNull();
        assertThat(response.identify()).isEqualTo(request.getIdentify());

        final var clientSut = entityManager.find(Client.class, response.identify());
        assertThat(clientSut).isNotNull();
        assertThat(clientSut.getId()).isEqualTo(request.getIdentify());
        assertThat(clientSut.getEmail()).isEqualTo(request.getEmail());
        assertThat(clientSut.getName()).isEqualTo(request.getName());

        final var accountSut = clientSut.getAccount();
        assertThat(accountSut).isNotNull();
        assertThat(accountSut.getId()).isNotNull();
        assertThat(accountSut.getType()).isEqualTo(AccountType.CLIENT);
        assertThat(accountSut.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(accountSut.getPassword()).isEqualTo(request.getPassword());
    }
}