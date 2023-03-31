package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateAmountDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
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
import java.util.Objects;
import java.util.UUID;

@Service
public class PassagemService {
    private final PassagemRepository passagemRepository;
    private final ObjectMapper objectMapper;
    private final CompanhiaService companhiaService;
    private final VooService vooService;
    private final LogService logService;
    private final UsuarioService usuarioService;

    public PassagemService(PassagemRepository passagemRepository, ObjectMapper objectMapper, CompanhiaService companhiaService,
                           @Lazy VooService vooService, LogService logService, UsuarioService usuarioService) {
        this.passagemRepository = passagemRepository;
        this.objectMapper = objectMapper;
        this.companhiaService = companhiaService;
        this.vooService = vooService;
        this.logService = logService;
        this.usuarioService = usuarioService;
    }

    public PassagemDTO create(PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {

        UUID codigo = UUID.randomUUID();

        //VALIDANDO VOO
        VooEntity vooEntity = vooService.getVoo(passagemCreateDTO.getIdVoo());
        vooService.validarCompanhiaLogada(vooEntity);
        vooEntity.setAssentosDisponiveis(vooEntity.getAssentosDisponiveis() - 1);

        //BUSCANDO A ÚLTIMA PASSAGEM CRIADA DAQUELE VOO
        PassagemEntity passagem = objectMapper.convertValue(passagemCreateDTO, PassagemEntity.class);
        Integer nAssento = passagemRepository.findByProximaPassagem(passagemCreateDTO.getIdVoo());

        passagem.setCodigo(codigo.toString());
        passagem.setStatus(Status.DISPONIVEL);
        passagem.setNumeroAssento(++nAssento);

        PassagemEntity passagemCriada = passagemRepository.save(passagem);
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagem.getIdPassagem());
        logService.saveLog(companhiaEntity, PassagemEntity.class, TipoOperacao.CRIAR);
        PassagemDTO passagemDTO = objectMapper.convertValue(passagemCriada, PassagemDTO.class);
        passagemDTO.setNomeCompanhia(companhiaEntity.getNome());

        //ATUALIZANDO O NUMERO DE ASSENTOS DISPONIVEIS DAQUELE VOO
        vooService.updateAssentosDisponiveis(vooEntity);
        return passagemDTO;
    }

    public List<PassagemDTO> createAmount(PassagemCreateAmountDTO passagemCreateAmountDTO) throws RegraDeNegocioException {

        List<PassagemDTO> passagemDTOS = new ArrayList<>();

        //VALIDANDO VOO
        VooEntity vooEntity = vooService.getVoo(passagemCreateAmountDTO.getIdVoo());
        vooService.validarCompanhiaLogada(vooEntity);
        vooEntity.setAssentosDisponiveis(vooEntity.getAssentosDisponiveis() - passagemCreateAmountDTO.getQuantidadeDePassagens());

        //BUSCANDO A ÚLTIMA PASSAGEM CRIADA DAQUELE VOO
        Integer nPassagem = passagemRepository.findByProximaPassagem(passagemCreateAmountDTO.getIdVoo());
        CompanhiaEntity companhiaEntity = companhiaService.getCompanhiaComId(vooEntity.getAviao().getIdCompanhia());

        //CRIANDO PASSAGENS SOLICITADAS
        for(int i = 0; i < passagemCreateAmountDTO.getQuantidadeDePassagens(); i ++){
            PassagemEntity passagemEntity = objectMapper.convertValue(passagemCreateAmountDTO, PassagemEntity.class);
            passagemEntity.setTipoAssento(passagemCreateAmountDTO.getTipoAssento());
            passagemEntity.setStatus(Status.DISPONIVEL);
            passagemEntity.setCodigo(UUID.randomUUID().toString());
            passagemEntity.setNumeroAssento(++nPassagem);
            PassagemDTO passagemDTO = objectMapper.convertValue(passagemRepository.save(passagemEntity), PassagemDTO.class);
            logService.saveLog(companhiaEntity, PassagemEntity.class, TipoOperacao.CRIAR);
            passagemDTO.setNomeCompanhia(companhiaEntity.getNome());
            passagemDTOS.add(passagemDTO);
        }

        //ATUALIZANDO O NUMERO DE ASSENTOS DISPONIVEIS DAQUELE VOO
        vooService.updateAssentosDisponiveis(vooEntity);
        return passagemDTOS;
    }

    public PassagemDTO update(Integer passagemId, PassagemCreateDTO passagemCreateDTO) throws RegraDeNegocioException {
        PassagemEntity passagemEncontrada = passagemRepository.findById(passagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));

        //VALIDANDO VOO
        vooService.getVoo(passagemCreateDTO.getIdVoo());
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagemEncontrada.getIdPassagem());

        // Validar companhia
        validarCompanhiaLogada(passagemEncontrada);

        if(passagemEncontrada.getStatus() == Status.CANCELADO){
            throw new RegraDeNegocioException("Passagem cancelada, não é possível editar!");
        }

        passagemEncontrada.setValor(passagemCreateDTO.getValor());
        passagemEncontrada.setTipoAssento(passagemCreateDTO.getTipoAssento());
        passagemEncontrada.setIdVoo(passagemCreateDTO.getIdVoo());
        PassagemDTO passagemDTO = objectMapper.convertValue(passagemRepository.save(passagemEncontrada), PassagemDTO.class);
        logService.saveLog(companhiaEntity, PassagemEntity.class, TipoOperacao.ALTERAR);
        passagemDTO.setNomeCompanhia(companhiaEntity.getNome());

        return passagemDTO;
    }

    public void delete(Integer passagemId) throws RegraDeNegocioException {
        PassagemEntity passagem = getPassagem(passagemId);
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagem.getIdPassagem());

        if (passagem.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Passagem já cancelada!");
        }

        // Validar companhia
        validarCompanhiaLogada(passagem);

        passagemRepository.deleteById(passagemId);
        logService.saveLog(companhiaEntity, PassagemEntity.class, TipoOperacao.DELETAR);
    }

    public PassagemDTO getById(Integer id) throws RegraDeNegocioException {
        PassagemEntity passagemEntity = passagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));

        PassagemDTO passagemDTO = objectMapper.convertValue(passagemEntity, PassagemDTO.class);
        passagemDTO.setNomeCompanhia(recuperarCompanhia(passagemEntity.getIdPassagem()).getNome());
        return passagemDTO;
    }

    public PageDTO<PassagemDTO> getByValorMaximo(BigDecimal valorMaximo, Integer pagina, Integer tamanho){
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        return listaPaginada(passagemRepository
                .findAllByValorIsLessThanEqual(valorMaximo, solcitacaoPagina),
                pagina,
                tamanho);
    }

    public PageDTO<PassagemDTO> getByVoo(Integer idVoo, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        VooEntity vooEntity = vooService.getVoo(idVoo);
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        return listaPaginada(passagemRepository
                .findAllByVoo(vooEntity, solcitacaoPagina),
                pagina,
                tamanho);
    }

    public PageDTO<PassagemDTO> getUltimasPassagens(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
       return listaPaginada(passagemRepository.findAllByStatusIs(Status.DISPONIVEL, solcitacaoPagina), pagina, tamanho);
    }

    protected PageDTO<PassagemDTO> listaPaginada (Page<PassagemEntity> pagePassagemEntity, Integer pagina, Integer tamanho){
        List<PassagemDTO> listaPassagensDoVoo = pagePassagemEntity
                .map(passagem -> {
                    PassagemDTO passagemDTO = objectMapper.convertValue(passagem, PassagemDTO.class);
                    passagemDTO.setNomeCompanhia(recuperarCompanhia(passagem.getIdPassagem()).getNome());
                    return passagemDTO;
                }).toList();

        return new PageDTO<>(pagePassagemEntity.getTotalElements(),
                pagePassagemEntity.getTotalPages(),
                pagina,
                tamanho,
                listaPassagensDoVoo);
    }

    protected CompanhiaEntity recuperarCompanhia(Integer idPassagem) {
        return companhiaService.recuperarCompanhia("idPassagem", idPassagem);
    }

    protected PassagemEntity getPassagem(Integer idPassagem) throws RegraDeNegocioException {
        return passagemRepository.findById(idPassagem)
                .orElseThrow(() -> new RegraDeNegocioException("Passagem não encontrada!"));
    }

    protected boolean alteraDisponibilidadePassagem (PassagemEntity passagem, VendaEntity vendaEntity) {
        passagem.setStatus(Status.VENDIDA);
        passagem.setIdVenda(vendaEntity.getIdVenda());
        passagemRepository.save(passagem);
        CompanhiaEntity companhiaEntity = recuperarCompanhia(passagem.getIdPassagem());
        logService.saveLog(companhiaEntity, PassagemEntity.class, TipoOperacao.ALTERAR);
        return true;
    }

    protected void validarCompanhiaLogada(PassagemEntity passsagemEntity) throws RegraDeNegocioException {
        UsuarioDTO loggedUser = usuarioService.getLoggedUser();
        CompanhiaEntity companhia = recuperarCompanhia(passsagemEntity.getIdVoo());
        if (!Objects.equals(companhia.getIdUsuario(), loggedUser.getIdUsuario())
                && loggedUser.getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RegraDeNegocioException("Você não tem permissão de realizar essa operação: " +
                    "A passagem informado pertence à outra companhia!");
        }
    }
}
