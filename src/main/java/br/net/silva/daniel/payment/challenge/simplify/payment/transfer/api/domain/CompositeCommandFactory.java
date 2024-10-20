package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import java.util.List;

public interface CompositeCommandFactory {
    List<Command> create();
}
