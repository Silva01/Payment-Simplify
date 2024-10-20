package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import java.util.List;

public interface GroupCommandFactory {
    List<Command> create();
}
