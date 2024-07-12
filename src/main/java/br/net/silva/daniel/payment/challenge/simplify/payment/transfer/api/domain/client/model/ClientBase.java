package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.model;

import lombok.Getter;

@Getter
public abstract class ClientBase {
    private final String email;
    private final String name;
    private final Account account;

    protected ClientBase(String email, String name, Account account) {
        this.email = email;
        this.name = name;
        this.account = account;
    }
}
