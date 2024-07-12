package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.enuns.AccountType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class ShopkeeperRequest extends BaseRequest {

    @NotBlank(message = "Attribute identify is required")
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
