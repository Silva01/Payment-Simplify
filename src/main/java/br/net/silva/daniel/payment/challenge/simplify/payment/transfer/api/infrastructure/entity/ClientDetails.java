package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDetails {

    @Column(unique = true, nullable = false, table = "client_details")
    private String email;

    @Column(table = "client_details")
    private String name;
}
