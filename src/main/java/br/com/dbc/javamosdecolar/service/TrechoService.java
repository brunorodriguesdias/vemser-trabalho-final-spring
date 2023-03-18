package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.TrechoCreateDTO;
import br.com.dbc.javamosdecolar.dto.TrechoDTO;
import br.com.dbc.javamosdecolar.entity.TrechoEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.TrechoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrechoService {

    private final TrechoRepository trechoRepository;
    private final ObjectMapper objectMapper;

    public List<TrechoDTO> getAll() throws RegraDeNegocioException {
        List<TrechoDTO> listaTrechos = trechoRepository.findAll().stream()
                .map(trecho -> {
                    TrechoDTO trechoDTO = objectMapper.convertValue(trecho, TrechoDTO.class);
                    return trechoDTO;
                })
                .toList();

        return listaTrechos;
    }

    public TrechoDTO create(TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {

        // Checa se a companhia já cadastrou esse trecho
        if (trechoRepository.findAllByOrigemIsAndDestinoIs(trechoDTO.getOrigem().toUpperCase(),
                trechoDTO.getDestino().toUpperCase()).isPresent()) {
            throw new RegraDeNegocioException("Trecho já existe!");
        }
        TrechoEntity trecho = objectMapper.convertValue(trechoDTO, TrechoEntity.class);

        return objectMapper
                .convertValue(trechoRepository.save(trecho), TrechoDTO.class);
    }

    public TrechoDTO update(Integer idTrecho, TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {
            trechoRepository.findById(idTrecho)
                    .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));

            if(trechoRepository.findAllByOrigemIsAndDestinoIs(trechoDTO.getOrigem().toUpperCase(),
                    trechoDTO.getDestino().toUpperCase()).isPresent()) {
                throw new RegraDeNegocioException("Trecho já existe!");
            }

            TrechoEntity trechoEditado = objectMapper.convertValue(trechoDTO, TrechoEntity.class);
            trechoEditado.setIdTrecho(idTrecho);

            trechoRepository.save(trechoEditado);

            return objectMapper.convertValue(trechoEditado, TrechoDTO.class);
    }

    public void delete(Integer idTrecho) throws RegraDeNegocioException {
        trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));
        trechoRepository.deleteById(idTrecho);
    }

//    public List<TrechoDTO> getByCompanhia(Integer idCompanhia) throws RegraDeNegocioException {
//        // Checa se companhia existe
//        CompanhiaEntity companhia = companhiaService.getCompanhia(idCompanhia);
//
//        return trechoRepository.findAllByCompanhia(companhia).stream()
//                .map(trecho -> objectMapper.convertValue(trecho, TrechoDTO.class))
//                .toList();
//    }

    public TrechoDTO getById(Integer idTrecho) throws RegraDeNegocioException {
        TrechoEntity trecho = trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Aconteceu algum problema durante a listagem."));

        return objectMapper.convertValue(trecho, TrechoDTO.class);
    }
}
