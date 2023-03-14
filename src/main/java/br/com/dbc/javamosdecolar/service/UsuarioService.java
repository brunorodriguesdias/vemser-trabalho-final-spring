package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.exception.DatabaseException;

import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.UsuarioEntity;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import br.com.dbc.javamosdecolar.repository.UsuarioRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
//    private final EmailService emailService;

    public UsuarioEntity create(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
//        try{
//            if (usuarioRepository.getByLogin(usuarioEntity.getLogin()).isEmpty()) {
//            emailService.sendEmail(usuarioEntity);
            return usuarioRepository.save(usuarioEntity);

//            } else {
//                throw new RegraDeNegocioException("Não foi possível concluir o cadastro.");
//            }

//        } catch (DatabaseException e) {
//            e.printStackTrace();
//            throw new RegraDeNegocioException("Aconteceu algum problema durante o cadastro");
//        }
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
