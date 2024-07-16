package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons.AbstractWebTest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest extends AbstractWebTest {

    private static final String URL = "/transfer";

    @Test
    void transferValue_WithValidData_TransferWithSuccess() throws Exception {
        final var request = new TransferRequest(BigDecimal.valueOf(100.0), 4L, 15L);
        postRequest(URL, request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.transactionId").isString());
    }


}
