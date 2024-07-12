package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseRequest {

    @NotBlank(message = "Attribute email is required")
    private final String email;

    @NotBlank(message = "Attribute name is required")
    private final String name;

    @NotBlank(message = "Attribute password is required")
    private final String password;

    protected abstract AccountType type();

    protected abstract String identify();
}
