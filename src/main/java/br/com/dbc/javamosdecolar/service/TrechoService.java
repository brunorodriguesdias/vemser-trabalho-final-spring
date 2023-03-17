package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.TrechoCreateDTO;
import br.com.dbc.javamosdecolar.dto.TrechoDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.TrechoEntity;
import br.com.dbc.javamosdecolar.repository.TrechoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrechoService {

    private final TrechoRepository trechoRepository;
    private final CompanhiaService companhiaService;
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
        CompanhiaEntity companhiaEntity = objectMapper
                .convertValue(companhiaService.getById(trechoDTO.getIdCompanhia()),
                        CompanhiaEntity.class);

        if (!companhiaEntity.getAtivo()) {
            throw new RegraDeNegocioException("Companhia indisponível.");
        }

        // Checa se a companhia já cadastrou esse trecho
        if (trechoRepository.findAllByOrigemIsAndDestinoIsAndIdCompanhiaIs(trechoDTO.getOrigem().toUpperCase(),
                trechoDTO.getDestino().toUpperCase(), trechoDTO.getIdCompanhia()).isPresent()) {
            throw new RegraDeNegocioException("Trecho já existe!");
        }
        TrechoEntity trecho = objectMapper.convertValue(trechoDTO, TrechoEntity.class);

        TrechoDTO trechoNovo = objectMapper
                .convertValue(trechoRepository.save(trecho), TrechoDTO.class);
//        trechoNovo.setIdCompanhia(companhiaEntity.getIdUsuario());

        return trechoNovo;
    }

    public TrechoDTO update(Integer idTrecho, TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {
            trechoRepository.findById(idTrecho)
                    .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));


            //companhiaService.getCompanhia(trechoDTO.getIdCompanhia());

            if(trechoRepository.findAllByOrigemIsAndDestinoIsAndIdCompanhiaIs(trechoDTO.getOrigem().toUpperCase(),
                    trechoDTO.getDestino().toUpperCase(), trechoDTO.getIdCompanhia()).isPresent()) {
                throw new RegraDeNegocioException("Trecho já existe!");
            }

            TrechoEntity trechoEditado = objectMapper.convertValue(trechoDTO, TrechoEntity.class);
            trechoEditado.setIdTrecho(idTrecho);

            trechoRepository.save(trechoEditado);
            TrechoDTO trechoEditadoDTO = objectMapper.convertValue(trechoEditado, TrechoDTO.class);

            return trechoEditadoDTO;
    }

    public void delete(Integer idTrecho) throws RegraDeNegocioException {
        trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));
        trechoRepository.deleteById(idTrecho);
    }

    public List<TrechoDTO> getByCompanhia(Integer idCompanhia) throws RegraDeNegocioException {
        // Checa se companhia existe
        CompanhiaEntity companhia = companhiaService.getCompanhia(idCompanhia);

        return trechoRepository.findAllByCompanhia(companhia).stream()
                .map(trecho -> objectMapper.convertValue(trecho, TrechoDTO.class))
                .toList();
    }

    public TrechoDTO getById(Integer idTrecho) throws RegraDeNegocioException {

        TrechoEntity trecho = trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Aconteceu algum problema durante a listagem."));

        TrechoDTO trechoDTO = objectMapper.convertValue(trecho, TrechoDTO.class);
//        trechoDTO.setIdCompanhia(trecho.getCompanhia().getIdUsuario());

        return trechoDTO;
    }
}
