package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.SimplifyPaymentTransferApiApplication;
import org.springframework.boot.SpringApplication;

public class TestSimplifyPaymentTransferApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(SimplifyPaymentTransferApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
