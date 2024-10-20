package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Command;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandInvoker;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandsResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

public class DummyInvoker extends CommandInvoker {

    public DummyInvoker() {
        super();
    }

    public DummyInvoker add(Command command) {
        return this;
    }

    @Override
    public CommandsResponse start() throws BadTransferException {
        throw new BadTransferException("Invalid transfer", 400);
    }
}
