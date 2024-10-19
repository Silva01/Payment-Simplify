package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import java.util.ArrayList;
import java.util.List;

public record CommandsResponse(
        List<CommandResponse> responses
) {

    public CommandsResponse() {
        this(new ArrayList<>());
    }

    public void add(CommandResponse response) {
        responses.add(response);
    }

    public CommandResponse getResponseForFlux(CommandResponseFluxEnum flux) {
        return responses.stream()
                .filter(response -> response.flux().equals(flux))
                .findFirst()
                .orElse(null);
    }
}
