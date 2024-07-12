package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class ShopkeeperRequest extends BaseRequest {

    @Size(min = 14, max = 14, message = "Attribute identify must have 14 characters")
    @NotNull
    @Getter
    private final String cnpj;

    public ShopkeeperRequest(String email, String name, String password, String cnpj) {
        super(email, name, password);
        this.cnpj = cnpj;
    }

    @Override
    public AccountType type() {
        return AccountType.SHOPKEEPER;
    }

    @Override
    public String identify() {
        return cnpj;
    }
}
