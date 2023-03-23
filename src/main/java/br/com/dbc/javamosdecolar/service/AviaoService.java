package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AviaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AviaoService {

    private final AviaoRepository aviaoRepository;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;

    public AviaoDTO create(AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {

        companhiaService.getCompanhia(aviaoCreateDTO.getIdCompanhia());

        AviaoEntity aviao = objectMapper.convertValue(aviaoCreateDTO, AviaoEntity.class);

        AviaoEntity aviaoCriada = aviaoRepository.save(aviao);

        return objectMapper.convertValue(aviaoCriada, AviaoDTO.class);
    }

    public AviaoDTO update(Integer aviaoId, AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {
        AviaoEntity aviaoEncontrada = aviaoRepository.findById(aviaoId)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));

        companhiaService.getCompanhia(aviaoCreateDTO.getIdCompanhia());

        /// update

        return objectMapper.convertValue(aviaoRepository.save(aviaoEncontrada), AviaoDTO.class);
    }

    public void delete(Integer aviaoId) throws RegraDeNegocioException {
        AviaoEntity aviao = getAviao(aviaoId);

        aviaoRepository.deleteById(aviaoId);
    }

    public AviaoDTO getById(Integer id) throws RegraDeNegocioException {
        AviaoDTO aviaoDTO = objectMapper.convertValue(aviaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!")), AviaoDTO.class);
        return aviaoDTO;
    }

    protected AviaoEntity getAviao(Integer idAviao) throws RegraDeNegocioException {
        return aviaoRepository.findById(idAviao)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));
    }

}
