package br.com.dbc.javamosdecolar.secutiry;

import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
//@RequiredArgsConstructor
//public class AuthenticationService implements UserDetailsService {
//
//    private final UsuarioService usuarioService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<UsuarioEntity> usuarioEntityOptional = usuarioService.findByLogin(username);
//        return usuarioEntityOptional
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario inválido!"));
//    }
//}
