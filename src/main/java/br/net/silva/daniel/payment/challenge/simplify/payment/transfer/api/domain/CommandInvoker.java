package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

import java.util.ArrayList;
import java.util.List;

public class CommandInvoker {

    private final List<Command> commands;

    public CommandInvoker() {
        this.commands = new ArrayList<>();
    }

    public static CommandInvoker of() {
        return new CommandInvoker();
    }

    public CommandInvoker add(Command command) {
        commands.add(command);
        return this;
    }

    public CommandInvoker add(CompositeCommandFactory commandFactory) {
        commands.addAll(commandFactory.create());
        return this;
    }

    public CommandsResponse start() throws BadTransferException {
        final var responses = new CommandsResponse();
        for (Command command : commands) {
            responses.add(command.execute());
        }

        return responses;
    }
}
