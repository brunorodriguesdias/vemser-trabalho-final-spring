package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.repository.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper;

    public PageDTO<AvaliacaoDTO> findAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
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

    public AvaliacaoDTO findByIdAvaliacao(Integer idAvaliacao) {
        return  objectMapper.convertValue(avaliacaoRepository.findByIdAvaliacao(idAvaliacao), AvaliacaoDTO.class);
    }

    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO){
        AvaliacaoEntity avaliacao = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        return objectMapper.convertValue(avaliacaoRepository.save(avaliacao), AvaliacaoDTO.class);
    }

    public void delete(Integer idAvaliacao){
        avaliacaoRepository.deleteById(String.valueOf(idAvaliacao));
    }
}
