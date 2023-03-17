package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompradorDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.CompradorEntity;
import br.com.dbc.javamosdecolar.model.TipoUsuario;
import br.com.dbc.javamosdecolar.repository.CompradorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompradorService {

    private final CompradorRepository compradorRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    public List<CompradorDTO> getAll() throws RegraDeNegocioException {
        List<CompradorDTO> compradorDTOS = compradorRepository.findAll()
                .stream()
                .map(compradorEntity -> objectMapper.convertValue(compradorEntity, CompradorDTO.class))
                .toList();
        return compradorDTOS;
    }

    public CompradorDTO create(CompradorCreateDTO compradorDTO) throws RegraDeNegocioException {

        //editando e adicionando usuario ao comprador
        CompradorEntity comprador = objectMapper.convertValue(compradorDTO, CompradorEntity.class);
        comprador.setTipoUsuario(TipoUsuario.COMPRADOR);
        comprador.setSenha(compradorDTO.getSenha());
        comprador.setAtivo(true);

        //salvando no bd o novo comprador
        compradorRepository.save(comprador);
        return objectMapper.convertValue(comprador, CompradorDTO.class);
    }

    public CompradorDTO update(Integer idComprador, CompradorCreateDTO compradorCreateDTO) throws RegraDeNegocioException {
        //Retorna o comprador
        CompradorEntity compradorEntity = getComprador(idComprador);

        //alterando entidade
        compradorEntity.setLogin(compradorCreateDTO.getLogin());
        compradorEntity.setSenha(compradorCreateDTO.getSenha());
        compradorEntity.setNome(compradorCreateDTO.getNome());
        compradorEntity.setCpf(compradorCreateDTO.getCpf());

        //salvando no bd
        compradorRepository.save(compradorEntity);

        return objectMapper.convertValue(compradorEntity, CompradorDTO.class);

    }

    public void delete(Integer idComprador) throws RegraDeNegocioException {
        //procurando comprador pelo ID
        getById(idComprador);

        //deletando comprador do bd
        usuarioService.deleteById(idComprador);
    }

    public CompradorDTO getById(Integer idComprador) throws RegraDeNegocioException {
        CompradorEntity compradorEncontrado = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        return objectMapper.convertValue(compradorEncontrado, CompradorDTO.class);
    }

    CompradorEntity getComprador(Integer id) throws RegraDeNegocioException {
        return compradorRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrada"));
    }
}