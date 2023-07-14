package com.brenomorim.controlepedagogico.domain.usuario;

import com.brenomorim.controlepedagogico.domain.shared.Role;

public record DadosListagemUsuario(Long id, String email, Role role) {
    public DadosListagemUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getEmail(), usuario.getRole());
    }
}
