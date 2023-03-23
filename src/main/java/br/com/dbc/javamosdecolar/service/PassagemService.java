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
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;

    public PassagemDTO create(PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {

        UUID codigo = UUID.randomUUID();
        companhiaService.getCompanhia(passagemCreateDTO.getIdCompanhia());

        //Validando voo
//        vooService.getVoo(passagemCreateDTO.getIdVoo());
//        vooService.validAssento(passagemCreateDTO.getIdVoo(), passagemCreateDTO.getNumeroAssento());

        PassagemEntity passagem = objectMapper.convertValue(passagemCreateDTO, PassagemEntity.class);
        passagem.setCodigo(codigo.toString());
        passagem.setStatus(Status.DISPONIVEL);

        PassagemEntity passagemCriada = passagemRepository.save(passagem);

        return objectMapper.convertValue(passagemCriada, PassagemDTO.class);
    }

    public PassagemDTO update(Integer passagemId, PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {
        PassagemEntity passagemEncontrada = passagemRepository.findById(passagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));

        //Validando voo
//        vooService.getVoo(passagemCreateDTO.getIdVoo());
//        vooService.validAssento(passagemCreateDTO.getIdVoo(), passagemCreateDTO.getNumeroAssento());


        if(passagemEncontrada.getStatus() == Status.CANCELADO){
            throw new RegraDeNegocioException("Passagem cancelada, não é possível editar!");
        }

        companhiaService.getCompanhia(passagemCreateDTO.getIdCompanhia());

        passagemEncontrada.setValor(passagemCreateDTO.getValor());
        passagemEncontrada.setNumeroAssento(passagemCreateDTO.getNumeroAssento());
        passagemEncontrada.setTipoAssento(passagemCreateDTO.getTipoAssento());
        passagemEncontrada.setIdCompanhia(passagemCreateDTO.getIdCompanhia());
        passagemEncontrada.setIdVoo(passagemCreateDTO.getIdVoo());


        return objectMapper.convertValue(passagemRepository.save(passagemEncontrada), PassagemDTO.class);
    }

    public void delete(Integer passagemId) throws RegraDeNegocioException {
        PassagemEntity passagem = getPassagem(passagemId);

        //PRECISO SABER VIVER

        if (passagem.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Passagem já cancelada!");
        }

        passagemRepository.deleteById(passagemId);
    }

    public PassagemDTO getById(Integer id) throws RegraDeNegocioException {
        PassagemDTO passagemDTO = objectMapper.convertValue(passagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!")), PassagemDTO.class);
        passagemDTO.setNomeCompanhia(companhiaService.getCompanhia(passagemDTO.getIdCompanhia()).getNome());
        return passagemDTO;
    }

    public List<PassagemDTO> getByValorMaximo(BigDecimal valorMaximo){
        return passagemRepository.findAllByValorIsLessThanEqual(valorMaximo).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setNomeCompanhia(passagem.getCompanhia().getNome());
                    return passagemDTO;
                }).toList();
    }

    public List<PassagemDTO> getByCompanhia(Integer idCompanhia) throws RegraDeNegocioException {
        CompanhiaEntity companhiaEntity = companhiaService.getCompanhia(idCompanhia);
        return passagemRepository
                .findAllByCompanhia(companhiaEntity).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setNomeCompanhia(passagem.getCompanhia().getNome());
                    return passagemDTO;
                }).toList();
    }

    public PageDTO<PassagemDTO> getUltimasPassagens(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<PassagemEntity> listaPaginada = passagemRepository.findAllByStatusIs(Status.DISPONIVEL, solcitacaoPagina);
        List<PassagemDTO> listaDePassagensDisponiveis = listaPaginada.map(passagem -> {
            PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
            passagemDTO.setNomeCompanhia(passagem.getCompanhia().getNome());
            return passagemDTO;
        }).toList();

        return new PageDTO<>(listaPaginada.getTotalElements(),
                listaPaginada.getTotalPages(),
                pagina,
                tamanho,
                listaDePassagensDisponiveis);
    }

    //PASSAR VALIDAR DATAS PARA VOO
    private void validarDatas(LocalDateTime dataPartida, LocalDateTime dataChegada) throws RegraDeNegocioException {
        if (dataChegada.isBefore(dataPartida)) {
            throw new RegraDeNegocioException("Data inválida!");
        }
    }

    protected PassagemEntity getPassagem(Integer idPassagem) throws RegraDeNegocioException {
        return passagemRepository.findById(idPassagem)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
    }

    protected boolean alteraDisponibilidadePassagem (PassagemEntity passagem, VendaEntity vendaEntity) {
        passagem.setStatus(Status.VENDIDA);
        passagem.setVenda(vendaEntity);
        passagemRepository.save(passagem);
        return true;
    }
}
