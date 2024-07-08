package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseRequest {
    private final String email;
    private final String name;
    private final String password;
}
