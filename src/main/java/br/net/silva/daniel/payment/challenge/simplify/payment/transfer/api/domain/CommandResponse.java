package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

public record CommandResponse(
        Object response
) {

    public <T> T response(Class<T> type) {
        return type.cast(response);
    }

}
