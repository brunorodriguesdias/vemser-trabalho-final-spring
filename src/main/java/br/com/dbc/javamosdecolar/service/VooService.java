package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VooRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VooService {
    private final VooRepository vooRepository;

    private AviaoService aviaoService;
    private final ObjectMapper objectMapper;

    public VooDTO create(VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {

//        Validando o avião
        aviaoService.getAviao(vooCreateDTO.getIdAviao());
//        Validando data
        validarDatas(vooCreateDTO.getDataPartida(), vooCreateDTO.getDataPartida());

        VooEntity vooEntity = objectMapper.convertValue(vooCreateDTO, VooEntity.class);

        return objectMapper.convertValue(vooEntity, VooDTO.class);
    }

    private void validarDatas(LocalDateTime dataPartida, LocalDateTime dataChegada) throws RegraDeNegocioException {
        if (dataChegada.isBefore(dataPartida)) {
            throw new RegraDeNegocioException("Data inválida!");
        }
    }

    public VooDTO update(Integer vooId, VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        VooEntity vooEncontrado = vooRepository.findById(vooId)
                .orElseThrow(() -> new RegraDeNegocioException("Voô não encontrado!"));

        if (vooEncontrado.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Voô cancelado, não é possível editar!");
        }

        vooEncontrado.setOrigem(vooCreateDTO.getOrigem());
        vooEncontrado.setDestino(vooCreateDTO.getDestino());
        vooEncontrado.setDataPartida(vooCreateDTO.getDataPartida());
        vooEncontrado.setDataChegada(vooCreateDTO.getDataChegada());

        return objectMapper.convertValue(vooEncontrado, VooDTO.class);
    }

    public void delete(Integer idVoo) throws RegraDeNegocioException {
        VooEntity vooEntity = getVoo(idVoo);

        if (vooEntity.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Voô já cancelado!");
        }

        vooRepository.deleteById(idVoo);
    }

    protected VooEntity getVoo(Integer idVoo) throws RegraDeNegocioException {
        return vooRepository.findById(idVoo)
                .orElseThrow(() -> new RegraDeNegocioException("Voô não encontrado!"));
    }
}
