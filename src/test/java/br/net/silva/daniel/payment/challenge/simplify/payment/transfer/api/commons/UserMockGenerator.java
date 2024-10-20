package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.account.Account;
import com.github.javafaker.Faker;

public interface UserMockGenerator {

    Faker FAKER = new Faker();

    default Account createSimpleAccount() {
        return Account
                .createSimpleAccount(FAKER.number().digits(11), FAKER.name().fullName(), FAKER.internet().emailAddress(), FAKER.internet().password());
    }

    default Account createRetailerAccount() {
        return Account
                .createRetailerAccount(FAKER.number().digits(11), FAKER.name().fullName(), FAKER.internet().emailAddress(), FAKER.internet().password());
    }

    default Account createStaticSimpleAccount() {
        return Account
                .createSimpleAccount("12345600099", "Daniel", "teste@teste", "123");
    }

    default Account createStaticRetailerAccount() {
        return Account
                .createRetailerAccount("12345600099", "Daniel", "teste@teste", "123");
    }
}
