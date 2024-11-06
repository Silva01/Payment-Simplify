package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization;

public record Authorization(
        String success,
        DataAuthorization data
) {
}
