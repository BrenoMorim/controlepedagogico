package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.usuario.DadosCadastroUsuario;
import com.brenomorim.controlepedagogico.domain.usuario.DadosLoginUsuario;
import com.brenomorim.controlepedagogico.domain.usuario.TokenLoginUsuario;
import com.brenomorim.controlepedagogico.domain.usuario.UsuarioRepository;
import com.brenomorim.controlepedagogico.infra.security.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<TokenLoginUsuario> login(@RequestBody @Valid DadosLoginUsuario dados) {
         var resposta = service.logar(dados);
         return ResponseEntity.ok(resposta);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<TokenLoginUsuario> cadastro(@RequestBody @Valid DadosCadastroUsuario dados) {

        boolean jaExiste = usuarioRepository.findByEmail(dados.email()).isPresent();

        if (jaExiste) throw new RegraDeNegocioException("Já existe um usuário cadastrado com esse email");

        var resposta = service.cadastrar(dados);
        return ResponseEntity.ok(resposta);
    }

}
