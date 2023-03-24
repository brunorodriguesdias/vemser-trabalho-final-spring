package br.com.dbc.javamosdecolar.secutiry;

import br.com.dbc.javamosdecolar.dto.outs.LoginDTO;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticaticationManager;

    public AuthenticationService(UsuarioService usuarioService, @Lazy AuthenticationManager authenticaticationManager) {
        this.usuarioService = usuarioService;
        this.authenticaticationManager = authenticaticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioService.findByLogin(username);
        return usuarioEntityOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuario inválido!"));
    }

    public UsuarioEntity autenticar(LoginDTO loginDTO) throws RegraDeNegocioException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getLogin(),
                        loginDTO.getSenha());

        Authentication authentication;
        try {
            authentication =
                    authenticaticationManager.authenticate(usernamePasswordAuthenticationToken);

            Object principal = authentication.getPrincipal();
            UsuarioEntity usuarioEntity = (UsuarioEntity) principal;

            return usuarioEntity;
        } catch (BadCredentialsException ex) {
            throw new RegraDeNegocioException("Usuario não encontrado");
        }
    }
}
