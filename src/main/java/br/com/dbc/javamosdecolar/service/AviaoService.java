package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AviaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AviaoService {

    private final AviaoRepository aviaoRepository;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;

    public PageDTO<AviaoDTO> getAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<AviaoDTO> avioesPaginados = aviaoRepository.getAllAviaoWithCompanhia(solcitacaoPagina);

        return new PageDTO<>(avioesPaginados.getTotalElements(),
                avioesPaginados.getTotalPages(),
                pagina,
                tamanho,
                avioesPaginados.getContent());
    }

    public AviaoDTO create(AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {

        if(aviaoRepository.existsByCodigoAviao(aviaoCreateDTO.getCodigoAviao())) {
            throw new RegraDeNegocioException("Já existe avião com esse código!");
        }

        // VALIDANDO E RECUPERANDO COMPANHIA
        CompanhiaEntity companhia = companhiaService.getCompanhiaComId(aviaoCreateDTO.getIdCompanhia());

        AviaoEntity aviao = objectMapper.convertValue(aviaoCreateDTO, AviaoEntity.class);
        aviao.setAtivo(true);

        // SALVANDO REGISTRO E PREPARANDO RETORNO
        AviaoDTO aviaoDTO = objectMapper.convertValue(aviaoRepository.save(aviao)
                                                , AviaoDTO.class);
        aviaoDTO.setNomeCompanhia(companhia.getNomeFantasia());

        return aviaoDTO;
    }

    public AviaoDTO update(Integer aviaoId, AviaoCreateDTO aviaoCreateDTO) throws RegraDeNegocioException {
        // VALIDANDO
        AviaoEntity aviaoEncontrado = aviaoRepository.findById(aviaoId)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));

        if (!aviaoEncontrado.isAtivo()) {
            throw new RegraDeNegocioException("Avião inativo, impossível edita-lo!");
        }

        // RECUPERANDO COMPANHIA
        CompanhiaEntity companhia = companhiaService.getCompanhiaComId(aviaoCreateDTO.getIdCompanhia());

        // ATUALIZANDO
        aviaoEncontrado.setCapacidade(aviaoCreateDTO.getCapacidade());
        aviaoEncontrado.setIdCompanhia(aviaoCreateDTO.getIdCompanhia());
        aviaoEncontrado.setUltimaManutencao(aviaoCreateDTO.getUltimaManutencao());

        // SALVANDO REGISTRO E PREPARANDO RETORNO
        AviaoDTO aviaoDTO = objectMapper.convertValue(aviaoRepository.save(aviaoEncontrado)
                , AviaoDTO.class);
        aviaoDTO.setNomeCompanhia(companhia.getNomeFantasia());

        return aviaoDTO;
    }

    public void delete(Integer aviaoId) throws RegraDeNegocioException {
        AviaoEntity aviao = getAviao(aviaoId);

        if (!aviao.isAtivo()) {
            throw new RegraDeNegocioException("Avião já está inativo!");
        }
        aviaoRepository.delete(aviao);
    }

    public AviaoDTO getById(Integer id) throws RegraDeNegocioException {
        AviaoDTO aviaoDTO = objectMapper.convertValue(getAviao(id), AviaoDTO.class);
        aviaoDTO.setNomeCompanhia(companhiaService.getCompanhiaComId(aviaoDTO.getIdCompanhia()).getNomeFantasia());
        return aviaoDTO;
    }

    protected AviaoEntity getAviao(Integer idAviao) throws RegraDeNegocioException {
        return aviaoRepository.findById(idAviao)
                .orElseThrow(() -> new RegraDeNegocioException("Aviao não encontrado!"));
    }
}
