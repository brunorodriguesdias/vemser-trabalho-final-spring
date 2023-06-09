package br.com.dbc.javamosdecolar.secutiry;

import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private static final String CARGOS_CHAVE = "cargos";

    public String gerarToken(UsuarioEntity usuarioEncontrado) throws RegraDeNegocioException {
        Date now = Date.valueOf(LocalDate.now());
        Date dateExpiration = new Date(now.getTime() + Long.parseLong(expiration));

        List<String> cargos = usuarioEncontrado.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        String meuToken = Jwts.builder()
                .claim("login", usuarioEncontrado.getLogin())
                .claim(Claims.ID, String.valueOf(usuarioEncontrado.getIdUsuario()))
                .claim(CARGOS_CHAVE, cargos)
                .setIssuedAt(now)
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return meuToken;
    }

    public UsernamePasswordAuthenticationToken isValid(String meuToken) {
        try {
            if (meuToken == null) {
                return null;
            }
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(meuToken)
                    .getBody();
            String user = body.get(Claims.ID, String.class);
            if (user != null) {

                List<String> cargos = body.get(CARGOS_CHAVE, List.class);
                List<SimpleGrantedAuthority> cargosDoUsuario = cargos.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .toList();

                return new UsernamePasswordAuthenticationToken(user, null, cargosDoUsuario);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
