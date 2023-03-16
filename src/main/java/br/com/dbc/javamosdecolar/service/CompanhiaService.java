package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompanhiaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.CompanhiaEntity;
import br.com.dbc.javamosdecolar.model.TipoUsuario;
import br.com.dbc.javamosdecolar.model.UsuarioEntity;
import br.com.dbc.javamosdecolar.repository.CompanhiaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanhiaService {

    private final CompanhiaRepository companhiaRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public List<CompanhiaDTO> getAll() throws RegraDeNegocioException {
        List<CompanhiaDTO> companhiaDTOS = companhiaRepository.findAll()
                .stream()
                .map(companhiaEntity -> objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class))
                .toList();
        if(companhiaDTOS == null){
            throw new RegraDeNegocioException("Sem registros de companhias!");
        }
        return companhiaDTOS;
    }

    public CompanhiaDTO create(CompanhiaCreateDTO companhiaCreateDTODTO) throws RegraDeNegocioException {

            UsuarioEntity usuarioCriado = usuarioService.create(companhiaCreateDTODTO);
            CompanhiaEntity companhiaEntity = objectMapper.convertValue(companhiaCreateDTODTO, CompanhiaEntity.class);
            companhiaEntity.setIdUsuario(usuarioCriado.getIdUsuario());

            CompanhiaDTO companhiaCriada = objectMapper.convertValue(companhiaRepository.save(companhiaEntity),
                    CompanhiaDTO.class);
            companhiaCriada.setAtivo(usuarioCriado.isAtivo());

            return companhiaCriada;
    }

    public CompanhiaDTO update(Integer id, CompanhiaCreateDTO companhiaCreateDTO) throws RegraDeNegocioException {
        CompanhiaEntity companhiaEntity = companhiaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Companhia não existe."));

        companhiaEntity.setLogin(companhiaCreateDTO.getLogin());
        companhiaEntity.setSenha(companhiaCreateDTO.getSenha());
        companhiaEntity.setNome(companhiaCreateDTO.getNome());
        companhiaEntity.setNomeFantasia(companhiaCreateDTO.getNomeFantasia());
        companhiaEntity.setCnpj(companhiaCreateDTO.getCnpj());

        return objectMapper.convertValue(companhiaRepository.save(companhiaEntity), CompanhiaDTO.class);
    }

    public void delete(Integer idCompanhia) throws RegraDeNegocioException {
//        try {
            CompanhiaEntity companhiaEntityEncontrada = companhiaRepository.findById(idCompanhia)
                    .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada!"));

            usuarioService.delete(companhiaEntityEncontrada.getIdUsuario());

//        }catch (DatabaseException e) {
//            e.printStackTrace();
//            throw new RegraDeNegocioException("Aconteceu algum problema durante a listagem.");
//        }
    }

    public CompanhiaDTO getById(Integer id) throws RegraDeNegocioException {
//        try {
            CompanhiaEntity companhiaEntity = companhiaRepository.findById(id)
                    .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada"));

            return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);

//        } catch (DatabaseException e) {
//            e.printStackTrace();
//            throw new RegraDeNegocioException("Aconteceu algum problema durante a recuperação da companhia.");
//        }
    }

    public CompanhiaEntity getByNome(String nome) throws RegraDeNegocioException {
//        try {
//            return companhiaRepository.getByNome(nome)
//                    .orElseThrow(() -> new RegraDeNegocioException("Companhia não Encontrada"));
            return null;

//        } catch (DatabaseException e) {
//            e.printStackTrace();
//            throw new RegraDeNegocioException("Aconteceu algum problema durante a recuperação da companhia.");
//        }
    }

}
