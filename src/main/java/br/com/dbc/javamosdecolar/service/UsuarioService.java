package br.com.dbc.javamosdecolar.service;

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
}
