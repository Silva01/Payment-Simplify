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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    private AuthorizationService authorizationService;

    @Mock
    private RestClient.Builder restBuilder;

    @BeforeEach
    void setUp() {
        when(restBuilder.baseUrl(anyString())).thenReturn(restBuilder);
        when(restBuilder.build()).thenReturn(mock(RestClient.class));

        this.authorizationService = new AuthorizationService(restBuilder);
    }

    @Test
    void authorizateTransaction_WithSuccess() {
        final var authorizationMock = new Authorization("success", new DataAuthorization(true));

        when(restBuilder.build().get()).thenReturn(mock(RestClient.RequestHeadersUriSpec.class));
        when(restBuilder.build().get().retrieve()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(restBuilder.build().get().retrieve().toEntity(Authorization.class))
                .thenReturn(ResponseEntity.ok(authorizationMock));

        assertThatCode(() -> authorizationService.authorizateTransaction())
                .doesNotThrowAnyException();
    }

    @Test
    void authorizateTransaction_WithNotAuthorizated() {
        final var authorizationMock = new Authorization("fail", new DataAuthorization(false));

        when(restBuilder.build().get()).thenReturn(mock(RestClient.RequestHeadersUriSpec.class));
        when(restBuilder.build().get().retrieve()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(restBuilder.build().get().retrieve().toEntity(Authorization.class))
                .thenReturn(ResponseEntity.ok(authorizationMock));

        assertThatThrownBy(() -> authorizationService.authorizateTransaction())
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Transaction not authorized");
    }
}