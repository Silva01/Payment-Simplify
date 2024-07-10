package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import com.github.javafaker.Faker;

public final class ClientFakerBuilder {

    private final static Faker FAKER = new Faker();

    public static ClientRequest buildClientRequest() {
        return new ClientRequest(
                FAKER.internet().emailAddress(),
                FAKER.name().fullName(),
                FAKER.internet().password(),
                FAKER.number().digits(11)
        );
    }
}
