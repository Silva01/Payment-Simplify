package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandInvokerTest {

    @Test
    void executionCommand_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command = new Command<String>() {
            @Override
            public String execute() {
                return "Hello World!";
            }
        };

        // Act
        final var result = CommandInvoker.of(command)
                .start();

        // Assert
        assertThat(result).isEqualTo("Hello World!");
    }

    @Test
    void executionCommand_WithErrors() throws BadTransferException {
        // Arrange
        final var command = new Command<String>() {
            @Override
            public String execute() {
                var num = 1 / 0;
                return "Hello World!";
            }
        };

        // Act
        assertThatThrownBy(() -> CommandInvoker.of(command).start())
                .isInstanceOf(ArithmeticException.class);
    }
}