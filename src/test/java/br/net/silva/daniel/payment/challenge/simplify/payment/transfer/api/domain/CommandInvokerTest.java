package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandInvokerTest {

    @Test
    void executionCommand_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command = new Command() {
            @Override
            public CommandResponse execute() {
                return new CommandResponse("Hello World!");
            }
        };

        // Act
        final var result = new CommandInvoker().add(command)
                .start();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).response(String.class)).isEqualTo("Hello World!");
    }

    @Test
    void executionCommand_WithErrors() throws BadTransferException {
        // Arrange
        final var command = new Command() {
            @Override
            public CommandResponse execute() {
                var num = 1 / 0;
                return new CommandResponse("Hello World!");
            }
        };

        // Act
        assertThatThrownBy(() -> new CommandInvoker().add(command).start())
                .isInstanceOf(ArithmeticException.class);
    }
}