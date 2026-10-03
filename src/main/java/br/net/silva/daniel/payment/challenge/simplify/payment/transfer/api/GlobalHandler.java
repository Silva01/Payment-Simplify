package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.authorization.AuthorizationException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.transaction.TransactionNotAllowsException;
import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(TransactionNotAllowsException.class)
    public ResponseEntity<ErrorMessage> handleTransactionNotAllowsException(TransactionNotAllowsException e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(HttpStatus.CONFLICT.value(), "Resource already exists"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Validation error: " + e.getBindingResult().getFieldError().getDefaultMessage()));
    }
}
