package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClientRequest extends BaseRequest {

    @NotBlank(message = "Attribute identify is required")
    private final String identify;

    public ClientRequest(String email, String name, String password, String identify) {
        super(email, name, password);
        this.identify = identify;
    }
}
