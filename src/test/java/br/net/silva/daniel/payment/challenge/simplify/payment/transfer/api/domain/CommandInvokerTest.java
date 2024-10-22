package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.DummyErrorCommand;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.DummyIntegerCommand;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons.DummyStringCommand;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandInvokerTest {

    @Test
    void executionCommand_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command = new DummyStringCommand();

        // Act
        new CommandInvoker().add(command)
                .start();

        // Assert
        assertThat(command.getMessage()).isNotNull();
        assertThat(command.getMessage()).isEqualTo("Hello World!");
    }

    @Test
    void executionCommand_ProcessWithMoreTwoResponse_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command1 = new DummyStringCommand();

        final var command2 = new DummyIntegerCommand();

        final var commandInvoker = new CommandInvoker();
        commandInvoker.add(command1);
        commandInvoker.add(command2);

        // Act
        commandInvoker.start();

        // Assert
        assertThat(command1.getMessage()).isNotNull();
        assertThat(command2.getCount()).isNotNull();
        assertThat(command1.getMessage()).isEqualTo("Hello World!");
        assertThat(command2.getCount()).isEqualTo(100);
    }

    @Test
    void executionCommand_WithErrors() {
        // Arrange
        final var command = new DummyErrorCommand();

        // Act
        assertThatThrownBy(() -> new CommandInvoker().add(command).start())
                .isInstanceOf(BadTransferException.class);
    }

    @Test
    void executionCommand_ProcessCreatedWithFactory_WithoutErrors() throws BadTransferException {
        // Arrange

        final var command1 = new DummyStringCommand();
        final var command2 = new DummyIntegerCommand();

        final var commandFactory = new CompositeCommandFactory() {
            @Override
            public List<Command> create() {
                return List.of(
                        command1,
                        command2
                );
            }
        };

        final var commandInvoker = new CommandInvoker().add(commandFactory);

        // Act
        commandInvoker.start();

        // Assert
        assertThat(command1.getMessage()).isNotNull();
        assertThat(command2.getCount()).isNotNull();
        assertThat(command1.getMessage()).isEqualTo("Hello World!");
        assertThat(command2.getCount()).isEqualTo(100);
    }
}