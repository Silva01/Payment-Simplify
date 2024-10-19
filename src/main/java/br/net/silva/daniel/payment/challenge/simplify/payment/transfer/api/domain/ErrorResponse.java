package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

public record ErrorResponse(
        String message,
        int status
) {
}
