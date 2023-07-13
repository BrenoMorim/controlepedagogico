package com.brenomorim.controlepedagogico.infra.exception;

import com.brenomorim.controlepedagogico.domain.RegraDeNegocioException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity tratarErroValorDeEnumInvalido(HttpMessageNotReadableException ex) {
        var mensagem = ex.getMessage();
        var valoresValidos = mensagem.subSequence(mensagem.indexOf("["), mensagem.indexOf("]") + 1);
        return ResponseEntity.badRequest().body(
                Map.of("message", "Um valor inválido foi fornecido, o valor deve ser um desses valores válidos: " + valoresValidos)
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity tratarErroDeTipoInvalido() {
        return ResponseEntity.badRequest().body(Map.of("message", "Um valor foi fornecido com o tipo inválido, caso esteja utilizando algum Enum ou data, verifique se o valor passado está dentre as opções válidas e no formato correto"));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity tratarErroDeRegraDeNegocio(RegraDeNegocioException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity tratarErroDeRegistroJaExistente(EntityExistsException ex) {
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroDeAutenticacao(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}
