package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import org.junit.jupiter.api.Test;

import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandResponseFluxEnum.EXTRACT;
import static br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.CommandResponseFluxEnum.TRANSFER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandInvokerTest {

    @Test
    void executionCommand_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command = new Command() {
            @Override
            public CommandResponse execute() {
                return new CommandResponse("Hello World!", TRANSFER);
            }
        };

        // Act
        final var result = new CommandInvoker().add(command)
                .start();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.responses()).hasSize(1);
        assertThat(result.getResponseForFlux(TRANSFER).response(String.class)).isEqualTo("Hello World!");
    }

    @Test
    void executionCommand_ProcessWitmMoreTwoResponse_WithoutErrors() throws BadTransferException {
        // Arrange
        final var command1 = new Command() {
            @Override
            public CommandResponse execute() {
                return new CommandResponse("Hello World!", TRANSFER);
            }
        };

        final var command2 = new Command() {
            @Override
            public CommandResponse execute() {
                return new CommandResponse(999, EXTRACT);
            }
        };

        final var commandInvoker = new CommandInvoker();
        commandInvoker.add(command1);
        commandInvoker.add(command2);
        // Act
        final var result = commandInvoker.start();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.responses()).hasSize(2);
        assertThat(result.getResponseForFlux(TRANSFER).response(String.class)).isEqualTo("Hello World!");
        assertThat(result.getResponseForFlux(EXTRACT).response(Integer.class)).isEqualTo(999);
    }

    @Test
    void executionCommand_WithErrors() throws BadTransferException {
        // Arrange
        final var command = new Command() {
            @Override
            public CommandResponse execute() {
                var num = 1 / 0;
                return new CommandResponse("Hello World!", TRANSFER);
            }
        };

        // Act
        assertThatThrownBy(() -> new CommandInvoker().add(command).start())
                .isInstanceOf(ArithmeticException.class);
    }
}