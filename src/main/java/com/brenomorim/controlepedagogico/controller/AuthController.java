package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.exception.RegraDeNegocioException;
import com.brenomorim.controlepedagogico.domain.shared.Role;
import com.brenomorim.controlepedagogico.domain.usuario.*;
import com.brenomorim.controlepedagogico.infra.security.AuthenticationService;
import com.brenomorim.controlepedagogico.infra.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/auth")
@RestController
@Profile(value = {"prod", "default", "authtest"})
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthenticationService service;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<TokenLoginUsuario> login(@RequestBody @Valid DadosLoginUsuario dados) {
         var resposta = service.logar(dados);
         return ResponseEntity.ok(resposta);
    }

    @PostMapping("/cadastro")
    @PreAuthorize("hasRole('COORDENACAO') or hasRole('SECRETARIA')")
    public ResponseEntity<DadosListagemUsuario> cadastro(HttpServletRequest request, @RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {

        boolean jaExiste = usuarioRepository.findByEmail(dados.email()).isPresent();
        if (jaExiste) throw new RegraDeNegocioException("Já existe um usuário cadastrado com esse email");

        var usuario = jwtService.extrairUsuario(request);
        if (usuario.getRole() == Role.ROLE_SECRETARIA && dados.role() != Role.ROLE_ALUNO) {
            throw new RegraDeNegocioException("A secretaria só pode cadastrar usuários alunos no sistema");
        }

        service.cadastrar(dados);
        var uri = uriBuilder.path("/auth/usuarios/{email}").buildAndExpand(dados.email()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuarioRepository.findByEmail(dados.email()).get()));
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('COORDENACAO') or hasRole('SECRETARIA') or hasRole('PROFESSOR')")
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 15, sort={"email"}) Pageable paginacao,
                                                             @RequestParam(required = false) Role role) {
        var exemplo = new Usuario(null, null, null, role);
        return ResponseEntity.ok(usuarioRepository.findAll(Example.of(exemplo), paginacao).map(DadosListagemUsuario::new));
    }

    @GetMapping("/usuarios/{email}")
    @PreAuthorize("hasRole('COORDENACAO') or hasRole('SECRETARIA') or hasRole('PROFESSOR')")
    public ResponseEntity<DadosListagemUsuario> buscaPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(new DadosListagemUsuario(usuarioRepository.findByEmail(email).get()));
    }

    @DeleteMapping("/usuarios/{email}")
    @PreAuthorize("hasRole('COORDENACAO')")
    public ResponseEntity deletar(@PathVariable String email) {
        usuarioRepository.deleteByEmail(email);
        return ResponseEntity.noContent().build();
    }

}
