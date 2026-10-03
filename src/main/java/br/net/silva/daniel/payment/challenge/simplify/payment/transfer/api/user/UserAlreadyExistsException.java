package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String cpf) {
    super("User with CPF " + cpf + " already exists");
  }

  public static UserAlreadyExistsException withCpf(String cpf) {
    return new UserAlreadyExistsException("User with CPF " + cpf + " already exists");
  }

  public static UserAlreadyExistsException withEmail(String email) {
    return new UserAlreadyExistsException("User with email " + email + " already exists");
  }
}
