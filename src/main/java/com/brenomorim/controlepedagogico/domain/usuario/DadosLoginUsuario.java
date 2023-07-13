package com.brenomorim.controlepedagogico.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosLoginUsuario(@NotNull String email, @NotNull String senha) {
}
