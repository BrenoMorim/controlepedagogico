package com.brenomorim.controlepedagogico.infra.security;

import com.brenomorim.controlepedagogico.domain.exception.AutenticacaoException;
import com.brenomorim.controlepedagogico.domain.usuario.Usuario;
import com.brenomorim.controlepedagogico.domain.usuario.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Profile(value = {"prod", "default", "authtest"})
public class JwtService {
    @Value("${api.security.token.secret}")
    private String jwtSigningKey;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public String extrairUsername(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public String gerarToken(UserDetails userDetails) {
        return gerarToken(new HashMap<>(), userDetails);
    }

    public Usuario extrairUsuario(HttpServletRequest request) {
        var token = request.getHeader("AUTHORIZATION").replace("Bearer", "").replaceAll(" ", "");
        var email = this.extrairUsername(token);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AutenticacaoException("Usuário não encontrado, refaça seu login e tente novamente"));
    }

    public boolean validaToken(String token, UserDetails userDetails) {
        final String userName = extrairUsername(token);
        return (userName.equals(userDetails.getUsername())) && !tokenEstaExpirado(token);
    }

    private <T> T extrairClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extrairTodasClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String gerarToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private boolean tokenEstaExpirado(String token) {
        return extrairDataExpiracao(token).before(new Date());
    }

    private Date extrairDataExpiracao(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    private Claims extrairTodasClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}