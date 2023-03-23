package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AviaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AviaoService {

    private final AviaoRepository aviaoRepository;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;

    public PageDTO<AviaoDTO> getAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<AviaoEntity> avioesPaginados = aviaoRepository.findAll(solcitacaoPagina);

        List<AviaoDTO> avioes = avioesPaginados.getContent().stream()
                .map(pessoa -> objectMapper.convertValue(pessoa, AviaoDTO.class))
                .toList();

        return new PageDTO<>(avioesPaginados.getTotalElements(),
                avioesPaginados.getTotalPages(),
                pagina,
                tamanho,
                avioes);
    }

    public AviaoDTO create(AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {

        if(aviaoRepository.existsByCodigoAviao(aviaoCreateDTO.getCodigoAviao())) {
            throw new RegraDeNegocioException("Já existe avião com esse código!");
        }

        companhiaService.getCompanhia(aviaoCreateDTO.getIdCompanhia());

        AviaoEntity aviao = objectMapper.convertValue(aviaoCreateDTO, AviaoEntity.class);
        aviao.setAtivo(true);

        AviaoEntity aviaoCriada = aviaoRepository.save(aviao);

        return objectMapper.convertValue(aviaoCriada, AviaoDTO.class);
    }

    public AviaoDTO update(Integer aviaoId, AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {
        AviaoEntity aviaoEncontrado = aviaoRepository.findById(aviaoId)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));

        if (!aviaoEncontrado.isAtivo()) {
            throw new RegraDeNegocioException("Avião inativo, impossível edita-lo!");
        }

        companhiaService.getCompanhia(aviaoCreateDTO.getIdCompanhia());

        aviaoEncontrado.setCapacidade(aviaoCreateDTO.getCapacidade());
        aviaoEncontrado.setIdCompanhia(aviaoCreateDTO.getIdCompanhia());
        aviaoEncontrado.setUltimaManutencao(aviaoCreateDTO.getUltimaManutencao());

        return objectMapper.convertValue(aviaoRepository.save(aviaoEncontrado), AviaoDTO.class);
    }

    public void delete(Integer aviaoId) throws RegraDeNegocioException {
        AviaoEntity aviao = getAviao(aviaoId);

        if (!aviao.isAtivo()) {
            throw new RegraDeNegocioException("Avião já está inativo!");
        }
        aviaoRepository.delete(aviao);
    }

    public AviaoDTO getById(Integer id) throws RegraDeNegocioException {
        return objectMapper.convertValue(aviaoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!")), AviaoDTO.class);
    }

    protected AviaoEntity getAviao(Integer idAviao) throws RegraDeNegocioException {
        return aviaoRepository.findById(idAviao)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));
    }


}
