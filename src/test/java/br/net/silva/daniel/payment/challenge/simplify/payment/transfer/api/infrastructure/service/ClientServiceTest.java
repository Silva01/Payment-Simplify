package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ShopkeeperRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.Client;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.ClientFakerBuilder.buildClientRequest;
import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.ClientFakerBuilder.buildShopkeeperRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
        assertThat(response.identify()).isEqualTo(request.identify());

        final var clientSut = entityManager.find(Client.class, response.identify());
        assertThat(clientSut).isNotNull();
        assertThat(clientSut.getId()).isEqualTo(request.identify());
        assertThat(clientSut.getEmail()).isEqualTo(request.getEmail());
        assertThat(clientSut.getName()).isEqualTo(request.getName());

        final var accountSut = clientSut.getAccount();
        assertThat(accountSut).isNotNull();
        assertThat(accountSut.getId()).isNotNull();
        assertThat(accountSut.getType()).isEqualTo(AccountType.CLIENT);
        assertThat(accountSut.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(accountSut.getPassword()).isEqualTo(request.getPassword());
    }

    @Test
    void createClient_WithUsedCpf_ReturnsException() {
        final var request = buildClientRequest();
        final var request2 = new ClientRequest(
                "test@test.com",
                "Test",
                request.getPassword(),
                request.identify()
        );

        assertThatCode(() -> {
            service.create(request);
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            service.create(request2);
        }).isInstanceOf(ClientAlreadyExistsException.class)
                .hasMessage(String.format("Invalid Request: CPF/CNPJ %s already exists", request2.identify()));
    }

    @Test
    void createClient_WithUsedEmail_ReturnsException() {
        final var request = buildClientRequest();
        final var request2 = new ClientRequest(
                request.getEmail(),
                "Test",
                request.getPassword(),
                "00000000099"
        );

        assertThatCode(() -> {
            service.create(request);
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            service.create(request2);
        }).isInstanceOf(ClientAlreadyExistsException.class)
                .hasMessage(String.format("Invalid Request: email %s already exists", request2.getEmail()));
    }

    @Test
    void createShopkeeper_WithValidData_CreateWithSuccess() throws ClientAlreadyExistsException {
        final var request = buildShopkeeperRequest();

        final var response = service.create(request);
        assertThat(response).isNotNull();
        assertThat(response.accountId()).isNotNull();
        assertThat(response.identify()).isEqualTo(request.identify());

        final var clientSut = entityManager.find(Client.class, response.identify());
        assertThat(clientSut).isNotNull();
        assertThat(clientSut.getId()).isEqualTo(request.identify());
        assertThat(clientSut.getEmail()).isEqualTo(request.getEmail());
        assertThat(clientSut.getName()).isEqualTo(request.getName());

        final var accountSut = clientSut.getAccount();
        assertThat(accountSut).isNotNull();
        assertThat(accountSut.getId()).isNotNull();
        assertThat(accountSut.getType()).isEqualTo(AccountType.SHOPKEEPER);
        assertThat(accountSut.getBalance()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(accountSut.getPassword()).isEqualTo(request.getPassword());
    }

    @Test
    void createShopkeeper_WithUsedCpf_ReturnsException() {
        final var request = buildShopkeeperRequest();
        final var request2 = new ShopkeeperRequest(
                "test@test.com",
                "Test",
                request.getPassword(),
                request.identify()
        );

        assertThatCode(() -> {
            service.create(request);
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            service.create(request2);
        }).isInstanceOf(ClientAlreadyExistsException.class)
                .hasMessage(String.format("Invalid Request: CPF/CNPJ %s already exists", request2.identify()));
    }

    @Test
    void createShopkeeper_WithUsedEmail_ReturnsException() {
        final var request = buildShopkeeperRequest();
        final var request2 = new ShopkeeperRequest(
                request.getEmail(),
                "Test",
                request.getPassword(),
                "00000000099/0001-99"
        );

        assertThatCode(() -> {
            service.create(request);
        }).doesNotThrowAnyException();

        assertThatCode(() -> {
            service.create(request2);
        }).isInstanceOf(ClientAlreadyExistsException.class)
                .hasMessage(String.format("Invalid Request: email %s already exists", request2.getEmail()));
    }
}