package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponse {

    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String type;
}
