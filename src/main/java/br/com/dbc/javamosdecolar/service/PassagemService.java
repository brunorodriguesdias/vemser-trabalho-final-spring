package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.PassagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        companhiaService.getCompanhia(passagemDTO.getIdCompanhia());

        validarDatas(passagemDTO.getDataPartida(), passagemDTO.getDataChegada());

        trechoService.getById(passagemDTO.getIdTrecho());

        PassagemEntity passagem = objectMapper.convertValue(passagemDTO, PassagemEntity.class);
        passagem.setCodigo(codigo.toString());

        passagem.setStatus(Status.DISPONIVEL);
        PassagemEntity passagemCriada = passagemRepository.save(passagem);

        return objectMapper.convertValue(passagemCriada, PassagemDTO.class);
    }

    public PassagemEntity getPassagem(Integer idPassagem) throws RegraDeNegocioException {
        return passagemRepository.findById(idPassagem)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
    }

    private void validarDatas(LocalDateTime dataPartida, LocalDateTime dataChegada) throws RegraDeNegocioException {
        if (dataChegada.isBefore(dataPartida)) {
            throw new RegraDeNegocioException("Data inválida!");
        }
    }

    public PassagemDTO update(Integer passagemId, PassagemCreateDTO passagemDTO) throws RegraDeNegocioException {
        PassagemEntity passagemEncontrada = passagemRepository.findById(passagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
        validarDatas(passagemDTO.getDataPartida(), passagemDTO.getDataChegada());
        companhiaService.getCompanhia(passagemDTO.getIdCompanhia());

        passagemEncontrada.setIdTrecho(passagemDTO.getIdTrecho());
        passagemEncontrada.setIdCompanhia(passagemDTO.getIdCompanhia());
        passagemEncontrada.setValor(passagemDTO.getValor());
        passagemEncontrada.setDataChegada(passagemDTO.getDataChegada());
        passagemEncontrada.setDataPartida(passagemDTO.getDataPartida());

        return objectMapper.convertValue(passagemRepository.save(passagemEncontrada), PassagemDTO.class);
    }

    public void delete(Integer passagemId) throws RegraDeNegocioException {
        PassagemEntity passagem = getPassagem(passagemId);

        if (passagem.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Passagem já cancelada!");
        }

        passagemRepository.deleteById(passagemId);
    }

    public PassagemDTO getById(Integer id) throws RegraDeNegocioException {
        PassagemEntity passagem = passagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
        return objectMapper.convertValue(passagem, PassagemDTO.class);
    }

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

    public PageDTO<PassagemDTO> getUltimasPassagens(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<PassagemEntity> listaPaginada = passagemRepository.findAllByStatusIs(Status.DISPONIVEL, solcitacaoPagina);
        List<PassagemDTO> listaDePassagensDisponiveis = listaPaginada.map(passagem -> {
            PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
            passagemDTO.setIdTrecho(passagem.getTrecho().getIdTrecho());
            return passagemDTO;
        }).toList();

        return new PageDTO<>(listaPaginada.getTotalElements(),
                listaPaginada.getTotalPages(),
                pagina,
                tamanho,
                listaDePassagensDisponiveis);
        }

    public boolean alteraDisponibilidadePassagem (PassagemEntity passagem, VendaEntity vendaEntity) {
        passagem.setStatus(Status.VENDIDA);
        passagem.setVenda(vendaEntity);
        passagemRepository.save(passagem);
        return true;
    }
}
