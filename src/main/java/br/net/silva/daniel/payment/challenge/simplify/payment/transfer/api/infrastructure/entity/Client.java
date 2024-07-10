package br.net.silva.daniel.payment.challenge.simplify.payment.transfer.api.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@SecondaryTable(name = "client_details", pkJoinColumns = @PrimaryKeyJoinColumn(name = "client_cpf"))
@Table(name = "client")
public final class Client {

    @Id
    private String cpf;

    @Embedded
    private ClientDetails details;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(referencedColumnName = "client_identity")
    private Account account;
}
