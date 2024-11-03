package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService service;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void createTransferTransaction_WithValidData_ReturnsStatus200() throws Exception {

        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));

        mockMvc.perform(post("/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void createTransferTransaction_WithTransferPayerTypeLojista_ReturnsStatus400WithMessageError() throws Exception {

        final var request = new TransactionRequest(2L, 1L, BigDecimal.valueOf(100));

        doThrow(new TransactionNotAllowsException("Payer type is not allowed to make transfers"))
                .when(service).createTransferTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payer type is not allowed to make transfers"))
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createTransferTransaction_WithTransferPayerToHimself_ReturnsStatus400WithMessageError() throws Exception {

        final var request = new TransactionRequest(1L, 1L, BigDecimal.valueOf(100));

        doThrow(new TransactionNotAllowsException("Payer not allowed to make transfers to himself"))
                .when(service).createTransferTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payer not allowed to make transfers to himself"))
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createTransferTransaction_WithTransferPayerWithoutBalance_ReturnsStatus400WithMessageError() throws Exception {

        final var request = new TransactionRequest(1L, 2L, BigDecimal.valueOf(100));

        doThrow(new TransactionNotAllowsException("Payer without balance to make transfers"))
                .when(service).createTransferTransaction(any(TransactionRequest.class));

        mockMvc.perform(post("/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Payer without balance to make transfers"))
                .andExpect(jsonPath("$.cod").value(400));
    }
}
