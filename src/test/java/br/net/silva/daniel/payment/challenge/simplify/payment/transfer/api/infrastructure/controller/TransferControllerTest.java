package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotFoundException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountUnauthorizedTransactionException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountWithoutBalanceException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.AbstractWebTest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest extends AbstractWebTest {

    private static final String URL = "/transfer";

    @MockBean
    private TransferService service;

    @Test
    void transferValue_WithValidData_TransferWithSuccess() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 4L, 15L);
        postRequest(URL, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.transactionId").isString());
    }

    @Test
    void transferValue_WithAccountPayerNotExists_ReturnsError404() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 1L, 15L);
        when(service.transfer(request)).thenThrow(new AccountNotFoundException(String.format("Account with id %d not found", request.payer())));

        postRequest(URL, request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(String.format("Account with id %d not found", request.payer())));
    }

    @Test
    void transferValue_WithAccountPayeeNotExists_ReturnsError404() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 1L, 15L);
        when(service.transfer(request)).thenThrow(new AccountNotFoundException(String.format("Account with id %d not found", request.payee())));

        postRequest(URL, request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value(String.format("Account with id %d not found", request.payee())));
    }

    @Test
    void transferValue_WithAccountWithoutBalance_ReturnsError403() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 1L, 15L);
        when(service.transfer(request)).thenThrow(new AccountWithoutBalanceException(String.format("Account with id %d it's hasn't balance", request.payer())));

        postRequest(URL, request)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(403))
                .andExpect(jsonPath("$.message").value(String.format("Account with id %d it's hasn't balance", request.payer())));
    }

    @Test
    void transferValue_WithAccountUnauthorizedTransaction_ReturnsError401() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 1L, 15L);
        when(service.transfer(request)).thenThrow(new AccountUnauthorizedTransactionException(String.format("Account with id %d unauthorized", request.payer())));

        postRequest(URL, request)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value(String.format("Account with id %d unauthorized", request.payer())));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void transferValue_WithInvalidData_ReturnsError406(TransferRequest request) throws Exception {
        postRequest(URL, request)
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.code").value(406))
                .andExpect(jsonPath("$.message").value("Invalid Request: Request has fields with value empty or null"));

    }

    private static Stream<Arguments> provideInvalidData() {
        return Stream.of(
                Arguments.of(new TransferRequest(null, 4L, 15L)),
                Arguments.of(new TransferRequest(BigDecimal.ZERO, 4L, 15L)),
                Arguments.of(new TransferRequest(BigDecimal.valueOf(100.0), null, 15L)),
                Arguments.of(new TransferRequest(BigDecimal.valueOf(100.0), 4L, null))
        );
    }
}
