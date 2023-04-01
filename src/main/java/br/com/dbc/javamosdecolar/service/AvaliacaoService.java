package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AvaliacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final LogService logService;

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

    public PageDTO<AvaliacaoDTO> findAllByNome(Integer pagina, Integer tamanho, String nome) {
        Pageable solicitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<AvaliacaoEntity> compradoresPaginados = avaliacaoRepository.findAllByNomeContainingIgnoreCase(nome, solicitacaoPagina);

        List<AvaliacaoDTO> compradores = compradoresPaginados.getContent().stream()
                .map(avaliacaoEntity -> objectMapper.convertValue(avaliacaoEntity, AvaliacaoDTO.class))
                .toList();

        return new PageDTO<>(compradoresPaginados.getTotalElements(),
                compradoresPaginados.getTotalPages(),
                pagina,
                tamanho,
                compradores);
    }

    public AvaliacaoRelatorioDTO gerarRelatorio() {
        AvaliacaoRelatorioDTO avaliacaoRelatorioDTO = avaliacaoRepository.gerarRelatorioAvaliacoes();
        avaliacaoRelatorioDTO.setQtdUsuarios(usuarioService.getCountUsers());
        return avaliacaoRelatorioDTO;
    }

    public AvaliacaoDTO findByIdAvaliacao(String idAvaliacao) throws RegraDeNegocioException {
        AvaliacaoEntity avaliacao = avaliacaoRepository.findById(idAvaliacao)
                .orElseThrow(()-> new RegraDeNegocioException("Avaliação não encontrada!"));
        return  objectMapper.convertValue(avaliacao, AvaliacaoDTO.class);
    }

    public AvaliacaoDTO create(AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException {
        AvaliacaoEntity avaliacao = objectMapper.convertValue(avaliacaoCreateDTO, AvaliacaoEntity.class);
        AvaliacaoDTO avaliacaoDTO = objectMapper.convertValue(avaliacaoRepository.save(avaliacao), AvaliacaoDTO.class);
        logService.saveLog(usuarioService.getLoggedUserEntity(), AvaliacaoEntity.class, TipoOperacao.CRIAR);
        return avaliacaoDTO;
    }

    public void delete(String idAvaliacao) throws RegraDeNegocioException{
        findByIdAvaliacao(idAvaliacao);
        avaliacaoRepository.deleteById(idAvaliacao);
        logService.saveLog(usuarioService.getLoggedUserEntity(), AvaliacaoEntity.class, TipoOperacao.DELETAR);
    }
}
