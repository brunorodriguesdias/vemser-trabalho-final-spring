package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompradorDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.CompradorEntity;
import br.com.dbc.javamosdecolar.model.TipoUsuario;
import br.com.dbc.javamosdecolar.model.UsuarioEntity;
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

    public CompradorDTO update(Integer idComprador, CompradorCreateDTO compradorDTO) throws RegraDeNegocioException {
        // Retorna o comprador existente
        CompradorEntity comprador = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        // Cria usuario e passa os dados para edição
        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .idUsuario(comprador.getIdUsuario())
                .login(compradorDTO.getLogin())
                .senha(comprador.getSenha())
                .nome(comprador.getNome())
                .tipoUsuario(TipoUsuario.COMPRADOR)
                .build();

        UsuarioEntity usuarioEditado = usuarioService.update(comprador.getIdUsuario(), usuarioEntity);

        return objectMapper.convertValue(comprador, CompradorDTO.class);

    }

    public void delete(Integer idComprador) throws RegraDeNegocioException {
        CompradorEntity compradorEncontrado = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        usuarioService.deleteById(compradorEncontrado.getIdUsuario());
    }

    public CompradorDTO getById(Integer idComprador) throws RegraDeNegocioException {
        CompradorEntity compradorEncontrado = compradorRepository.findById(idComprador)
                .stream().findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        return objectMapper.convertValue(compradorEncontrado, CompradorDTO.class);
    }
}