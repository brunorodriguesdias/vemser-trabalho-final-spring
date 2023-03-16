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
        if (compradorDTOS == null) {
            throw new RegraDeNegocioException("Sem registros de companhias!");
        }
        return compradorDTOS;
    }

    public CompradorDTO create(CompradorCreateDTO compradorDTO) throws RegraDeNegocioException {

        //criando usuario e salvando no bd
        UsuarioEntity usuarioNovo = usuarioService.create(compradorDTO);

        //editando e adicionando usuario ao comprador
        CompradorEntity comprador = objectMapper.convertValue(compradorDTO, CompradorEntity.class);
        comprador.setIdUsuario(usuarioNovo.getIdUsuario());
        comprador.setUsuarioEntity(usuarioNovo);

        //convertendo e salvando no bd o novo comprador
        CompradorDTO compradorCriado = objectMapper.convertValue(compradorRepository.save(comprador),CompradorDTO.class);

        return compradorCriado;
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
        comprador.setUsuarioEntity(usuarioEditado);

        return objectMapper.convertValue(comprador, CompradorDTO.class);

    }

    public void delete(Integer idComprador) throws RegraDeNegocioException {
        CompradorEntity compradorEncontrado = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        usuarioService.delete(compradorEncontrado.getIdUsuario());
    }

    public CompradorDTO getById(Integer idComprador) throws RegraDeNegocioException {
        CompradorEntity compradorEncontrado = compradorRepository.findById(idComprador)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));

        return objectMapper.convertValue(compradorEncontrado, CompradorDTO.class);
    }
}
