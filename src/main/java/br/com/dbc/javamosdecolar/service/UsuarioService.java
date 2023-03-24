package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void deleteById(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    public void existsLogin(String login) throws RegraDeNegocioException {
        if(usuarioRepository.existsByLogin(login)){
            throw new RegraDeNegocioException("Este login já está cadastrado!");
        }
    }

//    public UsuarioEntity autenticar(LoginDTO loginDTO) throws RegraDeNegocioException {
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        loginDTO.getLogin(),
//                        loginDTO.getSenha());
//
//        Authentication authentication;
//        try {
//            authentication =
//                    authenticaticationManager.authenticate(usernamePasswordAuthenticationToken);
//        } catch (BadCredentialsException ex) {
//            throw new RegraDeNegocioException("Usuario não encontrado");
//        }
//
//        Object principal = authentication.getPrincipal();
//        UsuarioEntity usuarioEntity = (UsuarioEntity) principal;
//
//        return usuarioEntity;
//    }
//
//    public Integer getIdLoggedUser() {
//        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
//    }
//
//    public LoginDTO getLoggedUser() throws RegraDeNegocioException {
//        UsuarioEntity usuarioEntity = findByUsuario(getIdLoggedUser())
//                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
//        return objectMapper.convertValue(usuarioEntity, LoginDTO.class);
//    }
}
