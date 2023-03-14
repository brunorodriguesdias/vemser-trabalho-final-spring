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
//    private final EmailService emailService;

    public <T> UsuarioEntity create(T object) throws RegraDeNegocioException {
        UsuarioEntity usuarioNovo = new UsuarioEntity();

        if(object instanceof CompanhiaCreateDTO){
            usuarioNovo.setLogin(((CompanhiaCreateDTO) object).getLogin());
            usuarioNovo.setSenha(((CompanhiaCreateDTO) object).getSenha());
            usuarioNovo.setNome(((CompanhiaCreateDTO) object).getNome());
            usuarioNovo.setTipoUsuario(TipoUsuario.COMPANHIA);
        }

        if(object instanceof CompradorCreateDTO){
            usuarioNovo.setLogin(((CompradorCreateDTO) object).getLogin());
            usuarioNovo.setSenha(((CompradorCreateDTO) object).getSenha());
            usuarioNovo.setNome(((CompradorCreateDTO) object).getNome());
            usuarioNovo.setTipoUsuario(TipoUsuario.COMPRADOR);
        }

        return usuarioRepository.save(usuarioNovo);
    }

    public UsuarioEntity update(Integer id, UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
//        try {
            usuarioRepository.findById(id)
                    .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
            usuarioRepository.save(usuarioEntity);

            usuarioEntity.setIdUsuario(id);

            return usuarioEntity;

//        } catch (DatabaseException e) {
//            throw new RegraDeNegocioException("Ocorreu um problema durante a edição do cadastro.");
//        }
    }

    public void delete(Integer idUsuario) throws RegraDeNegocioException {
//        try {
            usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
            usuarioRepository.deleteById(idUsuario);

//        } catch (DatabaseException e) {
//            throw new RegraDeNegocioException("Ocorreu um problema durante a edição do cadastro.");
//        }
    }
}
