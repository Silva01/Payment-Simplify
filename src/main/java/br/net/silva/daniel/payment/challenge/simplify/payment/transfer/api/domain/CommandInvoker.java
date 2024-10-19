package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;

public class CommandInvoker {

    private final Command<?> command;

    private CommandInvoker(Command<?> command) {
        this.command = command;
    }

    public static <T> CommandInvoker of (Command<T> command) {
        return new CommandInvoker(command);
    }

    public Object start() throws BadTransferException {
        return command.execute();
    }
}
