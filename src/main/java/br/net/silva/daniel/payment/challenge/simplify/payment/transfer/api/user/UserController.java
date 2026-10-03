package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.user;

import br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @ApiResponse(responseCode = "201", description = "User created successfully", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))
    })
    @ApiResponse(responseCode = "400", description = "Validation error", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
    })
    @ApiResponse(responseCode = "409", description = "User already exists with same CPF or email", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
    })
    @Tag(name = "Users", description = "Create a new user (common or merchant)")
    @Operation(summary = "Create a new user",
            description = "Create a new user with name, CPF, email, password and type (COMMON or MERCHANT)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRequest request) {
        return service.createUser(request);
    }
}
