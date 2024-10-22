package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.commons;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.Command;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import lombok.Getter;

@Getter
public class DummyErrorCommand implements Command {

    private Integer count;

    @Override
    public void execute() throws BadTransferException {
        throw new BadTransferException("Test Error", 500);
    }
}
