package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    private NotificationConsumer notificationConsumer;

    @Mock
    private NotificationRestClient notificationRestClient;

    @BeforeEach
    void setUp() {
        this.notificationConsumer = new NotificationConsumer(notificationRestClient);
    }

    @Test
    void consumeMessage_WithSuccess() {
        final var messageMock = new Transaction(1L, 2L, 3L, BigDecimal.valueOf(1000), LocalDateTime.now());
        final var autorizationMessageService = new Notification("success", new NotificationData("success"));

        when(notificationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(notificationRestClient.exec().toEntity(Notification.class))
                .thenReturn(ResponseEntity.ok(autorizationMessageService));

        assertThatCode(() -> notificationConsumer.send(messageMock))
                .doesNotThrowAnyException();

        verify(notificationRestClient.exec(), times(1)).toEntity(Notification.class);
    }

    @Test
    void consumeMessage_ServiceUnavailable_ThrowsError() {
        final var messageMock = new Transaction(1L, 2L, 3L, BigDecimal.valueOf(1000), LocalDateTime.now());
        final var autorizationMessageService = new Notification("fail", new NotificationData("error"));

        when(notificationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(notificationRestClient.exec().toEntity(Notification.class))
                .thenReturn(ResponseEntity.ok(autorizationMessageService));

        assertThatThrownBy(() -> notificationConsumer.send(messageMock))
                .isInstanceOf(NotificationNotSendException.class)
                .hasMessage("Error to send notification");

        verify(notificationRestClient.exec(), times(1)).toEntity(Notification.class);
    }

    @Test
    void consumeMessage_ServiceUnavailableWithReturn500_ThrowsError() {
        final var messageMock = new Transaction(1L, 2L, 3L, BigDecimal.valueOf(1000), LocalDateTime.now());

        when(notificationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(notificationRestClient.exec().toEntity(Notification.class))
                .thenReturn(ResponseEntity.internalServerError().body(null));

        assertThatThrownBy(() -> notificationConsumer.send(messageMock))
                .isInstanceOf(NotificationNotSendException.class)
                .hasMessage("Error to send notification");

        verify(notificationRestClient.exec(), times(1)).toEntity(Notification.class);
    }

    @Test
    void consumeMessage_ServiceUnavailableWithReturn400_ThrowsError() {
        final var messageMock = new Transaction(1L, 2L, 3L, BigDecimal.valueOf(1000), LocalDateTime.now());

        when(notificationRestClient.exec()).thenReturn(mock(RestClient.ResponseSpec.class));
        when(notificationRestClient.exec().toEntity(Notification.class))
                .thenReturn(ResponseEntity.badRequest().body(null));

        assertThatThrownBy(() -> notificationConsumer.send(messageMock))
                .isInstanceOf(NotificationNotSendException.class)
                .hasMessage("Error to send notification");

        verify(notificationRestClient.exec(), times(1)).toEntity(Notification.class);
    }
}