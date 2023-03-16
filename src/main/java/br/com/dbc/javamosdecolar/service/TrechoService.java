package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.TrechoDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.TrechoEntity;
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
                    trechoDTO.setIdCompanhia(trecho.getCompanhiaEntity().getIdUsuario());
                    return trechoDTO;
                })
                .toList();

        return listaTrechos;
    }

//    public TrechoDTO create(TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {
//        CompanhiaEntity companhiaEntity = objectMapper
//                .convertValue(companhiaService.getById(trechoDTO.getIdCompanhia()),
//                        CompanhiaEntity.class);
//
//        if (!companhiaEntity.isAtivo()) {
//            throw new RegraDeNegocioException("Companhia indisponível.");
//        }
//
//        // Checa se a companhia já cadastrou esse trecho
//        if (trechoRepository.getOne(trechoDTO.getOrigem().toUpperCase(),
//                trechoDTO.getDestino().toUpperCase(), companhiaEntity).isPresent()) {
//            throw new RegraDeNegocioException("Trecho já existe!");
//        }
//        TrechoEntity trecho = objectMapper.convertValue(trechoDTO, TrechoEntity.class);
//        trecho.setCompanhiaEntity(companhiaEntity);
//
//        TrechoDTO trechoNovo = objectMapper
//                .convertValue(trechoRepository.create(trecho), TrechoDTO.class);
//        trechoNovo.setIdCompanhia(companhiaEntity.getIdCompanhia());
//
//        return trechoNovo;
//    }

//    public TrechoDTO update(Integer idTrecho, TrechoCreateDTO trechoDTO) throws RegraDeNegocioException {
//            trechoRepository.findById(idTrecho)
//                    .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));
//
//            CompanhiaEntity companhiaEntity = objectMapper
//                    .convertValue(companhiaService.getById(trechoDTO.getIdCompanhia()), CompanhiaEntity.class);
//
//            if(trechoRepository.getOne(trechoDTO.getOrigem().toUpperCase(),
//                    trechoDTO.getDestino().toUpperCase(), companhiaEntity).isPresent()) {
//                throw new RegraDeNegocioException("Trecho já existe!");
//            }
//
//            TrechoEntity trechoEditado = objectMapper.convertValue(trechoDTO, TrechoEntity.class);
//            trechoEditado.setIdTrecho(idTrecho);
//
//            if(trechoRepository.update(idTrecho, trechoEditado)){
//                TrechoDTO trechoEditadoDTO = objectMapper.convertValue(trechoEditado, TrechoDTO.class);
//                trechoEditadoDTO.setIdCompanhia(companhiaEntity.getIdCompanhia());
//
//                return trechoEditadoDTO;
//
//            } else {
//                throw new RegraDeNegocioException("Não foi possível completar a edição.");
//            }
//    }

    public void delete(Integer idTrecho) throws RegraDeNegocioException {
        trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Trecho não encontrado!"));
        trechoRepository.deleteById(idTrecho);
    }

//    public List<TrechoDTO> getByCompanhia(Integer idCompanhia) throws RegraDeNegocioException {
//        try {
//            // Checa se companhia existe
//            companhiaService.getById(idCompanhia);
//
//            return trechoRepository.getByCompanhia(idCompanhia).stream()
//                    .map(trecho -> objectMapper.convertValue(trecho, TrechoDTO.class))
//                    .toList();
//
//        } catch (DatabaseException e) {
//            throw new RegraDeNegocioException("Aconteceu algum problema durante a listagem.");
//        }
//    }

    public TrechoDTO getById(Integer idTrecho) throws RegraDeNegocioException {

        TrechoEntity trecho = trechoRepository.findById(idTrecho)
                .orElseThrow(() -> new RegraDeNegocioException("Aconteceu algum problema durante a listagem."));

        TrechoDTO trechoDTO = objectMapper.convertValue(trecho, TrechoDTO.class);
        trechoDTO.setIdCompanhia(trecho.getCompanhiaEntity().getIdUsuario());

        return trechoDTO;
    }
}
