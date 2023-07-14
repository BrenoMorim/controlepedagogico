package com.brenomorim.controlepedagogico.domain.livro;

import com.brenomorim.controlepedagogico.domain.shared.FaixaEtaria;
import com.brenomorim.controlepedagogico.domain.shared.Idioma;
import com.brenomorim.controlepedagogico.domain.shared.Nivel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="nome")
@Table(name="livros")
@Entity(name="Livro")
public class Livro {

    @Id
    private String nome;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    @Enumerated(EnumType.STRING)
    private FaixaEtaria faixaEtaria;
    @Enumerated(EnumType.STRING)
    private Nivel nivel;

}
