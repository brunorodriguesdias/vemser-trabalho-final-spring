package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.PassagemDTO;
import br.com.dbc.javamosdecolar.entity.*;
import br.com.dbc.javamosdecolar.exception.DatabaseException;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.PassagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PassagemService {
    private final PassagemRepository passagemRepository;
    private final TrechoService trechoService;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;

    public PassagemDTO create(PassagemCreateDTO passagemDTO) throws RegraDeNegocioException {

        UUID codigo = UUID.randomUUID();
        objectMapper.convertValue(companhiaService.getById(passagemDTO.getIdCompanhia()),
                        CompanhiaEntity.class);

        validarDatas(passagemDTO.getDataPartida(), passagemDTO.getDataPartida());

        trechoService.getById(passagemDTO.getIdTrecho());

        PassagemEntity passagem = objectMapper.convertValue(passagemDTO, PassagemEntity.class);
        passagem.setCodigo(codigo.toString());

        passagem.setStatus(Status.DISPONIVEL);
        PassagemEntity passagemCriada = passagemRepository.save(passagem);

        return objectMapper.convertValue(passagemCriada, PassagemDTO.class);
    }

    private void validarDatas(LocalDateTime dataPartida, LocalDateTime dataChegada) throws RegraDeNegocioException {
        if (dataChegada.isBefore(dataPartida)) {
            throw new RegraDeNegocioException("Data inválida!");
        }
    }

    public PassagemDTO update(Integer passagemId, PassagemCreateDTO passagemDTO) throws RegraDeNegocioException {
        PassagemEntity passagemEncontrada = passagemRepository.findById(passagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
        validarDatas(passagemDTO.getDataPartida(), passagemDTO.getDataPartida());

        CompanhiaEntity companhiaEntity = objectMapper
                .convertValue(companhiaService.getById(passagemDTO.getIdCompanhia()),
                        CompanhiaEntity.class);

        if (!companhiaEntity.getAtivo()) {
            throw new RegraDeNegocioException("Companhia indisponível.");
        }

//        if (!passagemEncontrada.getStatus().equals(Status.CANCELADO)) {
//            throw new RegraDeNegocioException("Edição indisponivel para uma passagem já comprada.");
//        }

        PassagemEntity passagem = objectMapper.convertValue(passagemDTO, PassagemEntity.class);
        passagem.setStatus(passagemEncontrada.getStatus());


        return objectMapper.convertValue(passagemRepository.save(passagem), PassagemDTO.class);
    }

    public void delete(Integer passagemId){
        passagemRepository.deleteById(passagemId);
    }

    public PassagemDTO getById(Integer id) throws RegraDeNegocioException {
        PassagemEntity passagem = passagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
        return objectMapper.convertValue(passagem, PassagemDTO.class);
    }

//    public List<PassagemDTO> getByData(String dataChegada, String dataPartida) throws RegraDeNegocioException {
//            return passagemRepository.getByDataPartida(parseStringEmLocalDateTime(dataPartida))
//                    .stream()
//                    .map(passagem -> {
//                        PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
//                        passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
//                        return passagemDTO;
//                    }).toList();
//
//            dataChegada = dataChegada.replace("-", "/");
//
//            return passagemRepository.getByDataChegada(parseStringEmLocalDateTime(dataChegada))
//                    .stream()
//                    .map(passagem -> {
//                        PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
//                        passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
//                        return passagemDTO;
//                    }).toList();
//    }

    public List<PassagemDTO> getByValorMaximo(BigDecimal valorMaximo){
        return passagemRepository.findAllByValorIsLessThanEqual(valorMaximo).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
                    return passagemDTO;
                }).toList();
    }

    public List<PassagemDTO> getByCompanhia(Integer idCompanhia) throws RegraDeNegocioException {
        CompanhiaEntity companhiaEntity = companhiaService.getCompanhia(idCompanhia);
        return passagemRepository
                .findAllByCompanhia(companhiaEntity).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
                    return passagemDTO;
                }).toList();
    }

    public List<PassagemDTO> getUltimasPassagens() {
        return passagemRepository.findAllByStatusIs(Status.DISPONIVEL)
                .stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
                    return passagemDTO;
                })
                .toList();
    }

    public boolean alteraDisponibilidadePassagem (PassagemEntity passagem, VendaEntity vendaEntity) {
        passagem.setStatus(Status.VENDIDA);
        passagem.setVenda(vendaEntity);
        passagemRepository.save(passagem);
        return true;
    }
}
