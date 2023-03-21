package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.PageDTO;
import br.com.dbc.javamosdecolar.dto.TrechoCreateDTO;
import br.com.dbc.javamosdecolar.dto.TrechoDTO;
import br.com.dbc.javamosdecolar.entity.Status;
import br.com.dbc.javamosdecolar.entity.TrechoEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.TrechoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrechoService {

    private final TrechoRepository trechoRepository;
    private final ObjectMapper objectMapper;

    public PageDTO<TrechoDTO> getAll(Integer pagina, Integer tamanho)  {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<TrechoEntity> listaPaginada = trechoRepository.findAll(solcitacaoPagina);
        List<TrechoDTO> listaDeTrechosDisponiveis = listaPaginada.map(trecho -> {
            TrechoDTO trechoDTO = objectMapper.convertValue(trecho, TrechoDTO.class);
            return trechoDTO;
        }).toList();

        return new PageDTO<>(listaPaginada.getTotalElements(),
            listaPaginada.getTotalPages(),
            pagina,
            tamanho,
            listaDeTrechosDisponiveis);
        }

    public TrechoDTO create(TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {

        // Checa se a companhia já cadastrou esse trecho
        if (trechoRepository.findAllByOrigemIsAndDestinoIs(trechoDTO.getOrigem().toUpperCase(),
                trechoDTO.getDestino().toUpperCase()).isPresent()) {
            throw new RegraDeNegocioException("Trecho já existe!");
        }
        TrechoEntity trecho = objectMapper.convertValue(trechoDTO, TrechoEntity.class);
        trecho.setStatus(Status.DISPONIVEL);

        return objectMapper
                .convertValue(trechoRepository.save(trecho), TrechoDTO.class);
    }

    public TrechoDTO update(Integer idTrecho, TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {

            TrechoEntity trecho = trechoRepository.findById(idTrecho)
                    .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));

            if(trechoRepository.findAllByOrigemIsAndDestinoIs(trechoDTO.getOrigem().toUpperCase(),
                    trechoDTO.getDestino().toUpperCase()).isPresent()) {
                throw new RegraDeNegocioException("Trecho já existe!");
            }

            trecho.setDestino(trechoDTO.getDestino());
            trecho.setOrigem(trechoDTO.getDestino());

            return objectMapper.convertValue(trechoRepository.save(trecho), TrechoDTO.class);
    }

    public void delete(Integer idTrecho) throws RegraDeNegocioException {
        TrechoEntity trecho = trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));

        if (trecho.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Trecho já indisponível!");
        }

        // Indisponibilizando cada passagem desse trecho que estava como disponível
        trecho.getPassagem().forEach(passagem ->
            {
                if (passagem.getStatus() == Status.DISPONIVEL) {
                    passagem.setStatus(Status.CANCELADO);
                }
            }
        );

        trechoRepository.save(trecho);
        trechoRepository.deleteById(idTrecho);
    }

    public TrechoDTO getById(Integer idTrecho) throws RegraDeNegocioException {
        TrechoEntity trecho = trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Aconteceu algum problema durante a listagem."));

        return objectMapper.convertValue(trecho, TrechoDTO.class);
    }
}
