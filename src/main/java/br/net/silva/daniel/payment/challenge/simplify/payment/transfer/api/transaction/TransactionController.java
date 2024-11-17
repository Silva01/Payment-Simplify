package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @ApiResponse(responseCode = "200", description = "Transaction created")
    @ApiResponse(responseCode = "400", description = "Transaction not allowed for this problem with business rules", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
    })
    @ApiResponse(responseCode = "401", description = "Authorization error or not authorized", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
    })
    @Tag(name = "Transaction", description = "Create a new transaction of transfer between common accounts and Lojista accounts")
    @Operation(summary = "Create a new transaction of transfer between common accounts and Lojista accounts",
            description = "Create a new transaction of transfer between common accounts and Lojista accounts")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createTransferTransaction(@RequestBody TransactionRequest request) throws TransactionNotAllowsException {
        service.createTransferTransaction(request);
    }
}
