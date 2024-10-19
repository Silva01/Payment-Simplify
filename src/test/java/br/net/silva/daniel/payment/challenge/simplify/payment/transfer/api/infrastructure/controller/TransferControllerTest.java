package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService service;

    @Test
    @DisplayName("Transferencia basica com sucesso")
    void transferValue_WithValidData_TransferFinishedWithSuccess() throws Exception {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(1000), 1, 2);

        // Act
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void transferValue_WithoutBalance_ReturnsStatus403() throws Exception {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(1000), 1, 2);

        doThrow(new BadTransferException("Saldo insuficiente", HttpStatus.FORBIDDEN.value()))
                .when(service).transferValue(any(TransferRequest.class));

        // Act
        mockMvc.perform(post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Saldo insuficiente"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void transferValue_WithoutClient_ReturnsStatus404() throws Exception {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(1000), 1, 2);

        doThrow(new BadTransferException("Cliente nao encontrado", HttpStatus.NOT_FOUND.value()))
                .when(service).transferValue(any(TransferRequest.class));

        // Act
        mockMvc.perform(post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente nao encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void transferValue_RetalierToClient_ReturnsStatus400() throws Exception {
        // Arrange
        final var request = new TransferRequest(BigDecimal.valueOf(1000), 1, 2);

        doThrow(new BadTransferException("Logista não tem permissão para transferir valor", HttpStatus.BAD_REQUEST.value()))
                .when(service).transferValue(any(TransferRequest.class));

        // Act
        mockMvc.perform(post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Logista não tem permissão para transferir valor"))
                .andExpect(jsonPath("$.status").value(400));
    }
}