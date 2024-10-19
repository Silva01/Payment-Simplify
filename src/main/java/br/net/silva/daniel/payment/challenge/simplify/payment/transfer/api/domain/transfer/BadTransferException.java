package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer;

import lombok.Getter;

public class BadTransferException extends Exception {

    @Getter
    private final Integer codError;

    public BadTransferException(String message, Integer codError) {
        super(message);
        this.codError = codError;
    }
}
