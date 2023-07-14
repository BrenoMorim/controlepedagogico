package com.brenomorim.controlepedagogico.domain.professor;

import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record DadosAtualizacaoProfessor(
        @Email String email,
        @Pattern(regexp = "^(\\+?\\d{1,3})?[ -]?((\\(\\d{2}\\))|\\d{2})?[ -]?9?\\d{4}[ -]?\\d{4}$") String telefone,
        CargoProfessor cargo,
        Idioma idioma) {
}
