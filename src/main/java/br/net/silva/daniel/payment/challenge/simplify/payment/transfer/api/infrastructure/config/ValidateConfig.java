package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.config;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Validate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.Account;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.AccountNotExistsValidate;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.annotation.TransferAccountValidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ValidateConfig {

    @Bean
    @TransferAccountValidate
    public List<Validate<Account>> transferAccountValidates() {
        return List.of(
                new AccountNotExistsValidate()
        );
    }
}
