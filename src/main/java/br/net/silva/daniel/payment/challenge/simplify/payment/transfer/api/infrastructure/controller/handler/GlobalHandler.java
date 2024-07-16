package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.handler;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.client.exception.ClientAlreadyExistsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountNotFoundException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.domain.transfer.exception.AccountWithoutBalanceException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ResponseError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final var response = new ResponseError(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid Request: Request has fields with value empty or null");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<Object> handleClientAlreadyExistsException(ClientAlreadyExistsException ex) {
        final var response = new ResponseError(HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex) {
        final var response = new ResponseError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccountWithoutBalanceException.class)
    public ResponseEntity<Object> handleAccountWithoutBalanceException(AccountWithoutBalanceException ex) {
        final var response = new ResponseError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
