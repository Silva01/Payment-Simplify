package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.handler;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.controller.response.ResponseError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ClientHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final var response = new ResponseError(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid Request: Request has fields with value empty or null");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }
}
