package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompanhiaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.TipoUsuario;
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
        return companhiaDTOS;
    }

    public CompanhiaDTO create(CompanhiaCreateDTO companhiaCreateDTO) throws RegraDeNegocioException {

        //editando e adicionando usuario ao comprador
        CompanhiaEntity companhiaEntity = objectMapper.convertValue(companhiaCreateDTO, CompanhiaEntity.class);
        companhiaEntity.setTipoUsuario(TipoUsuario.COMPANHIA);
        companhiaEntity.setSenha(companhiaCreateDTO.getSenha());
        companhiaEntity.setAtivo(true);

        //salvando no bd o novo comprador
        companhiaRepository.save(companhiaEntity);

        return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);
    }

    public CompanhiaDTO update(Integer id, CompanhiaCreateDTO companhiaCreateDTO) throws RegraDeNegocioException {
        //Retorna o companhia
        CompanhiaEntity companhiaEntity = getCompanhia(id);

        //alterando entidade
        companhiaEntity.setLogin(companhiaCreateDTO.getLogin());
        companhiaEntity.setSenha(companhiaCreateDTO.getSenha());
        companhiaEntity.setNome(companhiaCreateDTO.getNome());
        companhiaEntity.setNomeFantasia(companhiaCreateDTO.getNomeFantasia());
        companhiaEntity.setCnpj(companhiaCreateDTO.getCnpj());

        //salvando no bd
        companhiaRepository.save(companhiaEntity);

        return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);
    }

    public void delete(Integer idCompanhia) throws RegraDeNegocioException {
        //procurando companhia pelo ID
        getById(idCompanhia);

        //deletando companhia do bd
        usuarioService.deleteById(idCompanhia);
    }

    public CompanhiaDTO getById(Integer id) throws RegraDeNegocioException {
        CompanhiaEntity companhiaEntity = companhiaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada"));

        return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);

    }

    CompanhiaEntity getCompanhia(Integer id) throws RegraDeNegocioException {
        return companhiaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada"));
    }
}
