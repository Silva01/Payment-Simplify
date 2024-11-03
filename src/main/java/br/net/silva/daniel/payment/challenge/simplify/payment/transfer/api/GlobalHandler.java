package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionNotAllowsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(TransactionNotAllowsException.class)
    public ResponseEntity<ErrorMessage> handleTransactionNotAllowsException(TransactionNotAllowsException e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }
}
