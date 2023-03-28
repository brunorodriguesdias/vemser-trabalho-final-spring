package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper;

    public PageDTO<AvaliacaoDTO> findAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho, Sort.by(Sort.Direction.DESC, "nota"));
        Page<AvaliacaoEntity> compradoresPaginados = avaliacaoRepository.findAll(solcitacaoPagina);

        List<AvaliacaoDTO> compradores = compradoresPaginados.getContent().stream()
                .map(avaliacaoEntity -> objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class))
                .toList();

        return new PageDTO<>(compradoresPaginados.getTotalElements(),
                compradoresPaginados.getTotalPages(),
                pagina,
                tamanho,
                compradores);
    }

    public PageDTO<AvaliacaoDTO> findAllByNota(Integer pagina, Integer tamanho, Integer nota) {
        Pageable solicitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<AvaliacaoEntity> compradoresPaginados = avaliacaoRepository.findAllByNota(nota, solicitacaoPagina);

        List<AvaliacaoDTO> compradores = compradoresPaginados.getContent().stream()
                .map(avaliacaoEntity -> objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class))
                .toList();

        return new PageDTO<>(compradoresPaginados.getTotalElements(),
                compradoresPaginados.getTotalPages(),
                pagina,
                tamanho,
                compradores);
    }

    public AvaliacaoDTO findByIdAvaliacao(String idAvaliacao) throws RegraDeNegocioException {
        Optional<AvaliacaoEntity> avaliacaoDTO = avaliacaoRepository.findById(idAvaliacao);
        if(avaliacaoDTO.isEmpty()){
            throw new RegraDeNegocioException("Avaliação não encontrada!");
        }
        return  objectMapper.convertValue(avaliacaoDTO.get(), AvaliacaoDTO.class);
    }

    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO){
        AvaliacaoEntity avaliacao = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        return objectMapper.convertValue(avaliacaoRepository.save(avaliacao), AvaliacaoDTO.class);
    }

    public void delete(String idAvaliacao) throws RegraDeNegocioException{
        findByIdAvaliacao(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
    }
}
