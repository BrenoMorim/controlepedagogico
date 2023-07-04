package com.brenomorim.controlepedagogico.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DadosPessoais {

    private String nome;
    private String cpf;
    private String email;
    private String telefone;

}
