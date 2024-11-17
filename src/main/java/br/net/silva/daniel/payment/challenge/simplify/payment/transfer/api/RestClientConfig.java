package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationRestClient;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationRestClientConfig;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification.NotificationRestClient;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.notification.NotificationRestClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @AuthorizationRestClientConfig
    public AuthorizationRestClient getAuthorizationRest(RestClient.Builder restClientBuilder) {
        return new RestClientClientAdapter(restClientBuilder, "https://util.devi.tools/api/v2/authorize");
    }

    @Bean
    @NotificationRestClientConfig
    public NotificationRestClient getNotificationRestClient(RestClient.Builder restClientBuilder) {
        return new RestClientClientAdapter(restClientBuilder, "https://util.devi.tools/api/v1/notify");
    }
}
