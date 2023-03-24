package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VooRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VooService {
    private final VooRepository vooRepository;
    private final CompanhiaService companhiaService;
    private final AviaoService aviaoService;
    private final ObjectMapper objectMapper;

    public VooDTO create(VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {

        //VALIDANDO AVIAO
        AviaoEntity aviaoEntity = aviaoService.getAviao(vooCreateDTO.getIdAviao());

        //VALIDANDO DATA
        validarDatas(vooCreateDTO.getDataPartida(), vooCreateDTO.getDataPartida());

        //SALVANDO VOO NO BD
        VooEntity vooEntity = objectMapper.convertValue(vooCreateDTO, VooEntity.class);
        vooEntity.setStatus(Status.DISPONIVEL);
        vooEntity = vooRepository.save(vooEntity);

        VooDTO vooDTO = objectMapper.convertValue(vooEntity, VooDTO.class);
        vooDTO.setNomeCompanhia(recuperarCompanhia(vooDTO.getIdVoo()).getNome());

        return vooDTO;
    }

    public VooDTO update(Integer vooId, VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        VooEntity vooEncontrado = vooRepository.findById(vooId)
                .orElseThrow(() -> new RegraDeNegocioException("Voô não encontrado!"));

        if (vooEncontrado.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Voô cancelado, não é possível editar!");
        }

        vooEncontrado.setIdAviao(vooCreateDTO.getIdAviao());
        vooEncontrado.setOrigem(vooCreateDTO.getOrigem());
        vooEncontrado.setDestino(vooCreateDTO.getDestino());
        vooEncontrado.setDataPartida(vooCreateDTO.getDataPartida());
        vooEncontrado.setDataChegada(vooCreateDTO.getDataChegada());

        //UPDATE VOO
        vooRepository.save(vooEncontrado);

        VooDTO vooDTO = objectMapper.convertValue(vooEncontrado, VooDTO.class);
        vooDTO.setNomeCompanhia(recuperarCompanhia(vooDTO.getIdVoo()).getNome());
        return vooDTO;
    }

    public void delete(Integer idVoo) throws RegraDeNegocioException {
        VooEntity vooEntity = getVoo(idVoo);

        if (vooEntity.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Voô já cancelado!");
        }

        vooRepository.deleteById(idVoo);
    }

    public VooDTO getById(Integer idVoo) throws RegraDeNegocioException {
        VooEntity voo = getVoo(idVoo);
        VooDTO vooDTO = objectMapper.convertValue(voo, VooDTO.class);
        vooDTO.setNomeCompanhia(recuperarCompanhia(idVoo).getNome());
        return vooDTO;
    }

    public PageDTO<VooDTO> getByVooAviao(Integer idAviao, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return listaPaginada(vooRepository.findByIdAviao(idAviao, pageable), pagina, tamanho);
    }

    public PageDTO<VooDTO> getByVooCompanhia(Integer idCompanhia, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return listaPaginada(vooRepository.findVooIdCompanhia(idCompanhia, pageable), pagina, tamanho);
    }

    public PageDTO<VooDTO> getAllVoo(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return listaPaginada(vooRepository.findAll(pageable), pagina, tamanho);
    }

    private PageDTO<VooDTO> listaPaginada (Page<VooEntity> pageVooEntity, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        if(pageVooEntity.isEmpty()){
            throw new RegraDeNegocioException("Id não encontrado!");
        }

        List<VooDTO> listaVoo = pageVooEntity
                .map(voo -> {
                    VooDTO vooDTO = objectMapper.convertValue(voo, VooDTO.class);
                    vooDTO.setNomeCompanhia(recuperarCompanhia(voo.getIdVoo()).getNome());
                    return vooDTO;
                }).toList();

        return new PageDTO<>(pageVooEntity.getTotalElements(),
                pageVooEntity.getTotalPages(),
                pagina,
                tamanho,
                listaVoo);
    }

    private CompanhiaEntity recuperarCompanhia(Integer idVoo) {
        return companhiaService.recuperarCompanhia("idVoo", idVoo);
    }

    private void validarDatas(LocalDateTime dataPartida, LocalDateTime dataChegada) throws RegraDeNegocioException {
        if (dataChegada.isBefore(dataPartida)) {
            throw new RegraDeNegocioException("Data inválida!");
        }
    }

    protected VooEntity getVoo(Integer idVoo) throws RegraDeNegocioException {
        return vooRepository.findById(idVoo)
                .orElseThrow(() -> new RegraDeNegocioException("Voô não encontrado!"));
    }

    protected VooEntity updateAssentosDisponiveis(VooEntity vooEntity) {
        return vooRepository.save(vooEntity);
    }
}
