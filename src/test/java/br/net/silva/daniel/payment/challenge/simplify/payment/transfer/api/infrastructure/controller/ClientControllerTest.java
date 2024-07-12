package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ClientResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @Test
    void createClient_WithValidData_CreateClientWithSuccess() throws Exception {
        final var request = new ClientRequest(
                "00099988877",
                "test@test.com",
                "Test",
                "12345555566"
        );

        when(service.create(any(ClientRequest.class)))
                .thenReturn(new ClientResponse(1L, request.identify()));

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountId").isNumber())
                .andExpect(jsonPath("$.identify").value(request.identify()));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void createClient_WithInvalidData_ReturnsError406(ClientRequest request) throws Exception {
        mockMvc.perform(post("/clients")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.code").value(406))
                .andExpect(jsonPath("$.message").value("Invalid Request: Request has fields with value empty or null"));
    }

    @Test
    void createClient_WithClientWithEmailAlreadyExists_ReturnsError409() throws Exception {
        final var request = new ClientRequest("test@test.com", "Test", "1234555", "99988877766");

        when(service.create(any(ClientRequest.class)))
                .thenThrow(new ClientAlreadyExistsException(String.format("Invalid Request: Client with email %s already exists", request.getEmail())));


        mockMvc.perform(post("/clients")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value(String.format("Invalid Request: Client with email %s already exists", request.getEmail())));
    }

    @Test
    void createClient_WithClientWithIdentifyAlreadyExists_ReturnsError409() throws Exception {
        final var request = new ClientRequest("test@test.com", "Test", "1234555", "99988877766");

        when(service.create(any(ClientRequest.class)))
                .thenThrow(new ClientAlreadyExistsException(String.format("Client with Identify %s already exists", request.identify())));


        mockMvc.perform(post("/clients")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value(String.format("Client with Identify %s already exists", request.identify())));
    }

    private static Stream<ClientRequest> provideInvalidData() {
        return Stream.of(
                new ClientRequest("", "test", "1234566", "99988877766"),
                new ClientRequest(null, "test", "1234566", "99988877766"),
                new ClientRequest("test@test.com", "", "1234566", "99988877766"),
                new ClientRequest("test@test.com", null, "1234566", "99988877766"),
                new ClientRequest("test@test.com", "test", "", "99988877766"),
                new ClientRequest("test@test.com", "test", null, "99988877766"),
                new ClientRequest("test@test.com", "test", null, "999888777660000"),
                new ClientRequest("test@test.com", "test", null, "999"),
                new ClientRequest("test@test.com", "test", "1234566", ""),
                new ClientRequest("test@test.com", "test", "1234566", null)
        );
    }
}
