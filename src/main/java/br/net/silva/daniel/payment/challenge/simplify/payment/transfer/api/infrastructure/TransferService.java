package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.BadTransferException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public void transferValue(TransferRequest request) throws BadTransferException {}
}
