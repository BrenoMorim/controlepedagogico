package com.brenomorim.controlepedagogico.controller;

import com.brenomorim.controlepedagogico.domain.shared.Role;
import com.brenomorim.controlepedagogico.domain.usuario.*;
import com.brenomorim.controlepedagogico.infra.security.JwtService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("authtest")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<DadosCadastroUsuario> dadosCadastroUsuarioJson;
    @Autowired
    private JacksonTester<DadosLoginUsuario> dadosLoginUsuarioJson;
    @Autowired
    private JacksonTester<DadosListagemUsuario> dadosListagemUsuarioJson;
    @Autowired
    private JacksonTester<TokenLoginUsuario> tokenLoginUsuarioJson;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;

    @BeforeAll
    public void criarUsuarios() {
        var senha = new BCryptPasswordEncoder().encode("1234567");

        var secretaria = new Usuario(Long.valueOf(1), "secretaria@email.com", senha, Role.ROLE_SECRETARIA);
        this.usuarioRepository.save(secretaria);

        var coordenador = new Usuario(Long.valueOf(2), "coordenador@email.com", senha, Role.ROLE_COORDENACAO);
        this.usuarioRepository.save(coordenador);
    }

    @Test
    @DisplayName("Login pode ser realizado, retornando token JWT contendo as informações do usuário como payload")
    public void testeLogin() throws Exception {
        var dadosLogin = new DadosLoginUsuario("secretaria@email.com", "1234567");

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosLoginUsuarioJson.write(dadosLogin).getJson())
        ).andReturn().getResponse();
        var token = tokenLoginUsuarioJson.parse(resposta.getContentAsString()).getObject().token();
        var email = jwtService.extrairUsername(token);

        Assertions.assertEquals(HttpStatus.OK.value(), resposta.getStatus());
        Assertions.assertEquals(email, dadosLogin.email());
    }

    @Test
    @DisplayName("Em caso de dados inválido, login deve retornar código 401")
    public void testeLoginInvalido() throws Exception {
        var dadosLogin = new DadosLoginUsuario("emailinvalido@email.com", "1234567");

        var resposta = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosLoginUsuarioJson.write(dadosLogin).getJson())
        ).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), resposta.getStatus());
    }

    @Test
    @DisplayName("A secretaria só pode cadastrar usuários de role aluno")
    public void testeCadastroSecretaria() throws Exception {
        var dadosLogin = new DadosLoginUsuario("secretaria@email.com", "1234567");
        var dadosCadastroProfessor = new DadosCadastroUsuario("professor@email.com", "1234567", Role.ROLE_PROFESSOR);
        var dadosCadastroAluno = new DadosCadastroUsuario("aluno@email.com", "1234567", Role.ROLE_ALUNO);

        String resposta = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosLoginUsuarioJson.write(dadosLogin).getJson())
        ).andReturn().getResponse().getContentAsString();

        String token = tokenLoginUsuarioJson.parse(resposta).getObject().token();

        var respostaProfessor = mockMvc.perform(MockMvcRequestBuilders.post("/auth/cadastro")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroUsuarioJson.write(dadosCadastroProfessor).getJson()))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), respostaProfessor.getStatus());

        var respostaAluno = mockMvc.perform(MockMvcRequestBuilders.post("/auth/cadastro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCadastroUsuarioJson.write(dadosCadastroAluno).getJson())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), respostaAluno.getStatus());
    }

    @Test
    @DisplayName("A coordenação pode cadastrar usuários de qualquer role")
    public void testeCadastroCoordenacao() throws Exception {
        var dadosLogin = new DadosLoginUsuario("coordenador@email.com", "1234567");
        var dadosCadastroProfessor = new DadosCadastroUsuario("professor@email.com", "1234567", Role.ROLE_PROFESSOR);
        var dadosCadastroCoordenacao = new DadosCadastroUsuario("coordenador2@email.com", "1234567", Role.ROLE_COORDENACAO);

        String resposta = mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosLoginUsuarioJson.write(dadosLogin).getJson())
        ).andReturn().getResponse().getContentAsString();

        String token = tokenLoginUsuarioJson.parse(resposta).getObject().token();

        var respostaProfessor = mockMvc.perform(MockMvcRequestBuilders.post("/auth/cadastro")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroUsuarioJson.write(dadosCadastroProfessor).getJson()))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), respostaProfessor.getStatus());

        var respostaCoordenacao = mockMvc.perform(MockMvcRequestBuilders.post("/auth/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroUsuarioJson.write(dadosCadastroCoordenacao).getJson())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), respostaCoordenacao.getStatus());
    }

}
