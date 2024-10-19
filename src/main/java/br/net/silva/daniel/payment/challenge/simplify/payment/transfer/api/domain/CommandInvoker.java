package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {

    private final List<Command> commands;

    public CommandInvoker() {
        this.commands = new ArrayList<>();
    }

    public CommandInvoker add(Command command) {
        commands.add(command);
        return this;
    }

    public List<CommandResponse> start() throws BadTransferException {
        final List<CommandResponse> responses = new ArrayList<>();
        for (Command command : commands) {
            responses.add(command.execute());
        }

        return responses;
    }
}
