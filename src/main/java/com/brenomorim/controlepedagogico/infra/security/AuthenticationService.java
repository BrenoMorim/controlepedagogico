package com.brenomorim.controlepedagogico.infra.security;

import com.brenomorim.controlepedagogico.domain.exception.AutenticacaoException;
import com.brenomorim.controlepedagogico.domain.usuario.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile(value = {"prod", "dev", "default"})
public class AuthenticationService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenLoginUsuario cadastrar(DadosCadastroUsuario dados) {
        var usuario = Usuario.builder()
                .email(dados.email()).password(passwordEncoder.encode(dados.senha()))
                .role(dados.role()).build();
        usuarioRepository.save(usuario);
        var jwt = jwtService.gerarToken(usuario);
        return new TokenLoginUsuario(jwt);
    }

    public TokenLoginUsuario logar(DadosLoginUsuario dados) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dados.email(), dados.senha()));
        var user = usuarioRepository.findByEmail(dados.email())
                .orElseThrow(() -> new AutenticacaoException("Login ou senha inválidos, verifique seus dados e tente novamente"));
        var jwt = jwtService.gerarToken(user);
        return new TokenLoginUsuario(jwt);
    }
}
