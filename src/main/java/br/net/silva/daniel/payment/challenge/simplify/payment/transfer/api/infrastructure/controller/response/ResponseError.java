package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response;

public record ResponseError(
        int code,
        String message) {
}
