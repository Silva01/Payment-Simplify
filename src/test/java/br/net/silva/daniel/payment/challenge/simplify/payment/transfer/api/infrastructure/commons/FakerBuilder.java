package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ClientRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ShopkeeperRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.TransferRequest;
import com.github.javafaker.Faker;

import java.math.BigDecimal;

public final class FakerBuilder {

    private final static Faker FAKER = new Faker();

    public static class ClientFaker {
        public static ClientRequest buildClientRequest() {
            return new ClientRequest(
                    FAKER.internet().emailAddress(),
                    FAKER.name().fullName(),
                    FAKER.internet().password(),
                    FAKER.number().digits(11)
            );
        }

        public static ShopkeeperRequest buildShopkeeperRequest() {
            return new ShopkeeperRequest(
                    FAKER.internet().emailAddress(),
                    FAKER.name().fullName(),
                    FAKER.internet().password(),
                    FAKER.number().digits(14)
            );
        }
    }

    public static class TransferFaker {
        public static TransferRequest buildTransferRequest() {
            return new TransferRequest(
                    BigDecimal.valueOf(FAKER.number().randomDouble(2, 0, 1000)),
                    FAKER.number().randomNumber(),
                    FAKER.number().randomNumber()
            );
        }
    }
}
