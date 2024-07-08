package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import lombok.Getter;

@Getter
public class ClientRequest extends BaseRequest {

    private final String cpf;

    public ClientRequest(String email, String name, String password, String cpf) {
        super(email, name, password);
        this.cpf = cpf;
    }
}
