package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class ClientRequest extends BaseRequest {

    @Size(min = 11, max = 11, message = "Attribute identify must have 11 characters")
    @NotNull
    @Getter
    private final String cpf;

    public ClientRequest(String email, String name, String password, String cpf) {
        super(email, name, password);
        this.cpf = cpf;
    }

    @Override
    public AccountType type() {
        return AccountType.CLIENT;
    }

    @Override
    public String identify() {
        return cpf;
    }
}
