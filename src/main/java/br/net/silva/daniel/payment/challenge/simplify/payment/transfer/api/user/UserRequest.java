package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.wallet.WalletTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CPF is required")
    private String cpf;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Type is required")
    private WalletTypeEnum type;
}
