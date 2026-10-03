package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.Wallet;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletRepository;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
    }

    @Test
    void createUser_WithValidCommonUserData_ReturnsStatus201AndCorrectResponse() throws Exception {
        var request = new UserRequest("John Doe", "12345678901", "john@example.com", "password123", WalletTypeEnum.COMUM);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.type").value("COMUM"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void createUser_WithValidMerchantUserData_ReturnsStatus201AndCorrectResponse() throws Exception {
        var request = new UserRequest("Jane Merchant", "98765432101", "merchant@example.com", "securepass", WalletTypeEnum.LOJISTA);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Jane Merchant"))
                .andExpect(jsonPath("$.cpf").value("98765432101"))
                .andExpect(jsonPath("$.email").value("merchant@example.com"))
                .andExpect(jsonPath("$.type").value("LOJISTA"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void createUser_WithDuplicateCpf_ReturnsStatus409Conflict() throws Exception {
        var existingUser = new Wallet(null, "Existing User", "12345678901", "existing@example.com", "password", BigDecimal.ZERO, WalletTypeEnum.COMUM);
        walletRepository.save(existingUser);

        var request = new UserRequest("New User", "12345678901", "newuser@example.com", "password123", WalletTypeEnum.COMUM);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(409));
    }

    @Test
    void createUser_WithDuplicateEmail_ReturnsStatus409Conflict() throws Exception {
        var existingUser = new Wallet(null, "Existing User", "12345678901", "duplicate@example.com", "password", BigDecimal.ZERO, WalletTypeEnum.COMUM);
        walletRepository.save(existingUser);

        var request = new UserRequest("New User", "98765432101", "duplicate@example.com", "password123", WalletTypeEnum.COMUM);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(409));
    }

    @Test
    void createUser_WithMissingName_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"cpf\": \"12345678901\", \"email\": \"test@example.com\", \"password\": \"password123\", \"type\": \"COMUM\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createUser_WithMissingCpf_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"name\": \"John Doe\", \"email\": \"test@example.com\", \"password\": \"password123\", \"type\": \"COMUM\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createUser_WithMissingEmail_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"name\": \"John Doe\", \"cpf\": \"12345678901\", \"password\": \"password123\", \"type\": \"COMUM\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createUser_WithMissingPassword_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"name\": \"John Doe\", \"cpf\": \"12345678901\", \"email\": \"test@example.com\", \"type\": \"COMUM\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createUser_WithMissingType_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"name\": \"John Doe\", \"cpf\": \"12345678901\", \"email\": \"test@example.com\", \"password\": \"password123\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.cod").value(400));
    }

    @Test
    void createUser_WithInvalidTypeValue_ReturnsStatus400BadRequest() throws Exception {
        var request = "{\"name\": \"John Doe\", \"cpf\": \"12345678901\", \"email\": \"test@example.com\", \"password\": \"password123\", \"type\": \"INVALID_TYPE\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithValidData_BalanceInitializedToZero() throws Exception {
        var request = new UserRequest("Balance Test", "11111111111", "balance@example.com", "password123", WalletTypeEnum.COMUM);

        var response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var userId = objectMapper.readTree(response.getResponse().getContentAsString()).get("id").asLong();

        var savedUser = walletRepository.findById(userId).orElseThrow();
        assert savedUser.getBalance().compareTo(BigDecimal.ZERO) == 0 : "Balance should be zero";
    }
}
