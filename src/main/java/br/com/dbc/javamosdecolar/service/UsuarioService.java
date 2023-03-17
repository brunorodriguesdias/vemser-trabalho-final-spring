package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.TipoUsuario;
import br.com.dbc.javamosdecolar.model.UsuarioEntity;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public <T> UsuarioEntity create(T object) throws RegraDeNegocioException {
        UsuarioEntity usuarioNovo = null;

        if(object instanceof CompanhiaCreateDTO){
            usuarioNovo = UsuarioEntity.builder()
                    .login(((CompanhiaCreateDTO) object).getLogin())
                    .senha(((CompanhiaCreateDTO) object).getSenha())
                    .nome(((CompanhiaCreateDTO) object).getNome())
                    .tipoUsuario(TipoUsuario.COMPANHIA)
                    .build();
        }

        if(object instanceof CompradorCreateDTO){
            usuarioNovo = UsuarioEntity.builder()
                    .login(((CompradorCreateDTO) object).getLogin())
                    .senha(((CompradorCreateDTO) object).getSenha())
                    .nome(((CompradorCreateDTO) object).getNome())
                    .tipoUsuario(TipoUsuario.COMPRADOR)
                    .build();
        }

        return usuarioNovo;
    }

    public UsuarioEntity update(Integer id, UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
            usuarioRepository.findById(id)
                    .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
            usuarioRepository.save(usuarioEntity);
            usuarioEntity.setIdUsuario(id);
            return usuarioEntity;

    }

    public void deleteById(Integer idUsuario) throws RegraDeNegocioException {
        usuarioRepository.deleteById(idUsuario);
    }
}
