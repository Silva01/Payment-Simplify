package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Command;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import lombok.Getter;

@Getter
public class DummyIntegerCommand implements Command {

    private Integer count;

    @Override
    public void execute() throws BadTransferException {
        this.count = 100;
    }
}
