package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.request.ShopkeeperRequest;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ClientResponse;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.service.ClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopkeeper")
@AllArgsConstructor
public class ShopkeeperController {

    private final ClientService service;

    @PostMapping
    public ResponseEntity<ClientResponse> create(@RequestBody @Valid ShopkeeperRequest request) throws ClientAlreadyExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}
