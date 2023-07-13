package com.brenomorim.controlepedagogico.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotNull @Email
        String email,
        @NotNull @Size(min = 7, max = 50)
        String senha,
        @NotNull
        Role role
) {
}
