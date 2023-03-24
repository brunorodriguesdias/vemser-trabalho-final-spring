package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateAmountDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.PassagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PassagemService {
    private final PassagemRepository passagemRepository;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;
    private final VooService vooService;
    private final AviaoService aviaoService;

    public PassagemService(PassagemRepository passagemRepository, ObjectMapper objectMapper, CompanhiaService companhiaService, @Lazy VooService vooService, @Lazy AviaoService aviaoService) {
        this.passagemRepository = passagemRepository;
        this.objectMapper = objectMapper;
        this.companhiaService = companhiaService;
        this.vooService = vooService;
        this.aviaoService = aviaoService;
    }

    public PassagemDTO create(PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {

        UUID codigo = UUID.randomUUID();
        VooEntity vooEntity = vooService.getVoo(passagemCreateDTO.getIdVoo());
        vooEntity.setAssentosDisponiveis(vooEntity.getAssentosDisponiveis() - 1);

        PassagemEntity passagem = objectMapper.convertValue(passagemCreateDTO, PassagemEntity.class);
        Integer nAssento = passagemRepository.findByProximaPassagem(passagemCreateDTO.getIdVoo());

        passagem.setCodigo(codigo.toString());
        passagem.setStatus(Status.DISPONIVEL);
        passagem.setNumeroAssento(++nAssento);

        PassagemEntity passagemCriada = passagemRepository.save(passagem);
        PassagemDTO passagemDTO = objectMapper.convertValue(passagemCriada, PassagemDTO.class);
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagem.getIdPassagem());
        passagemDTO.setNomeCompanhia(companhiaEntity.getNome());

        vooService.updateAssentosDisponiveis(vooEntity);

        return passagemDTO;
    }

    public List<PassagemDTO> createAmount(PassagemCreateAmountDTO passagemCreateAmountDTO) throws RegraDeNegocioException {

        List<PassagemDTO> passagemDTOS = new ArrayList<>();
        VooEntity vooEntity = vooService.getVoo(passagemCreateAmountDTO.getIdVoo());
        vooEntity.setAssentosDisponiveis(vooEntity.getAssentosDisponiveis() - passagemCreateAmountDTO.getQuantidadeDePassagens());

        Integer nPassagem = passagemRepository.findByProximaPassagem(passagemCreateAmountDTO.getIdVoo());

        for(int i = 0; i < passagemCreateAmountDTO.getQuantidadeDePassagens(); i ++){
            PassagemEntity passagemEntity = objectMapper.convertValue(passagemCreateAmountDTO, PassagemEntity.class);
            passagemEntity.setTipoAssento(passagemCreateAmountDTO.getTipoAssento());
            passagemEntity.setStatus(Status.DISPONIVEL);
            passagemEntity.setCodigo(UUID.randomUUID().toString());
            System.out.println(passagemEntity.getCodigo());
            passagemEntity.setNumeroAssento(++nPassagem);
            PassagemDTO passagemDTO = objectMapper.convertValue(passagemRepository.save(passagemEntity), PassagemDTO.class);
            passagemDTO.setNomeCompanhia(recuperarCompanhia(passagemEntity.getIdPassagem()).getNome());
            passagemDTOS.add(passagemDTO);
        }

        vooService.updateAssentosDisponiveis(vooEntity);
        return passagemDTOS;
    }

    public PassagemDTO update(Integer passagemId, PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {
        PassagemEntity passagemEncontrada = passagemRepository.findById(passagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));

        //Validando voo
        vooService.getVoo(passagemCreateDTO.getIdVoo());
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagemEncontrada.getIdPassagem());

        if(passagemEncontrada.getStatus() == Status.CANCELADO){
            throw new RegraDeNegocioException("Passagem cancelada, não é possível editar!");
        }

        passagemEncontrada.setValor(passagemCreateDTO.getValor());
        passagemEncontrada.setTipoAssento(passagemCreateDTO.getTipoAssento());
        passagemEncontrada.setIdVoo(passagemCreateDTO.getIdVoo());
        PassagemDTO passagemDTO = objectMapper.convertValue(passagemRepository.save(passagemEncontrada), PassagemDTO.class);
        passagemDTO.setNomeCompanhia(companhiaEntity.getNome());

        return passagemDTO;
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
        PassagemEntity passagemEntity = passagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));

        PassagemDTO passagemDTO = objectMapper.convertValue(passagemEntity, PassagemDTO.class);
        passagemDTO.setNomeCompanhia(recuperarCompanhia(passagemEntity.getIdPassagem()).getNome());
        return passagemDTO;
    }

    public List<PassagemDTO> getByValorMaximo(BigDecimal valorMaximo){
        return passagemRepository.findAllByValorIsLessThanEqual(valorMaximo).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setNomeCompanhia(recuperarCompanhia(passagem.getIdPassagem()).getNome());
                    return passagemDTO;
                }).toList();
    }

    public List<PassagemDTO> getByCompanhia(Integer idVoo) throws RegraDeNegocioException {
        VooEntity vooEntity = vooService.getVoo(idVoo);
        return passagemRepository
                .findAllByVoo(vooEntity).stream()
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setNomeCompanhia(recuperarCompanhia(passagem.getIdPassagem()).getNome());
                    return passagemDTO;
                }).toList();
    }

    public PageDTO<PassagemDTO> getUltimasPassagens(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<PassagemEntity> listaPaginada = passagemRepository.findAllByStatusIs(Status.DISPONIVEL, solcitacaoPagina);
        List<PassagemDTO> listaDePassagensDisponiveis = listaPaginada.map(passagem -> {
            PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
            passagemDTO.setNomeCompanhia(recuperarCompanhia(passagem.getIdPassagem()).getNome());
            return passagemDTO;
        }).toList();

        return new PageDTO<>(listaPaginada.getTotalElements(),
                listaPaginada.getTotalPages(),
                pagina,
                tamanho,
                listaDePassagensDisponiveis);
    }

    private CompanhiaEntity recuperarCompanhia(Integer idPassagem) {
        return companhiaService.recuperarCompanhiaPassagem(idPassagem);
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
