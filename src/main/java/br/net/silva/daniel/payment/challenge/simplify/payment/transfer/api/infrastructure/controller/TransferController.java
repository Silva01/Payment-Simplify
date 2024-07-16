package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.TransferResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @PostMapping
    public ResponseEntity<TransferResponse> transfer() {
        return ResponseEntity.ok(new TransferResponse(UUID.randomUUID().toString()));
    }
}
