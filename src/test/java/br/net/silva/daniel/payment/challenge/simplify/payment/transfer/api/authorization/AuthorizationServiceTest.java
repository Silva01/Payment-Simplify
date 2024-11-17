package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    @Mock
    private AuthorizationRestClient authorizationRestClient;

    @BeforeEach
    void setUp() {
        this.authorizationService = new AuthorizationService(authorizationRestClient);
    }

    @Test
    void authorizeTransaction_WithSuccess() {
        final var authorizationMock = new Authorization("success", new DataAuthorization(true));

        when(authorizationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(authorizationRestClient.exec().toEntity(Authorization.class))
                .thenReturn(ResponseEntity.ok(authorizationMock));

        assertThatCode(() -> authorizationService.authorizeTransaction())
                .doesNotThrowAnyException();
    }

    @Test
    void authorizeTransaction_WithNotAuthorizated() {
        final var authorizationMock = new Authorization("fail", new DataAuthorization(false));

        when(authorizationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(authorizationRestClient.exec().toEntity(Authorization.class))
                .thenReturn(ResponseEntity.ok(authorizationMock));

        assertThatThrownBy(() -> authorizationService.authorizeTransaction())
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Transaction not authorized");
    }
}