package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.dto.outs.VendaDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;
    private final PassagemService passagemService;
    private final CompradorService compradorService;
    private final CompanhiaService companhiaService;
//    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final LogService logService;

    public VendaDTO create(VendaCreateDTO vendaDTO) throws RegraDeNegocioException {

        UUID codigo = UUID.randomUUID();

        CompradorEntity compradorEntity = compradorService.getLoggedComprador();

        PassagemEntity passagem = passagemService.getPassagem(vendaDTO.getIdPassagem());
        if (passagem.getStatus() != Status.DISPONIVEL) {
            throw new RegraDeNegocioException("Passagem indisponível!");
        }

        CompanhiaEntity companhiaEntity = passagemService.recuperarCompanhia(vendaDTO.getIdPassagem());

        VendaEntity vendaEntity = objectMapper.convertValue(vendaDTO, VendaEntity.class);
        vendaEntity.setCodigo(String.valueOf(codigo));
        vendaEntity.setComprador(compradorEntity);
        vendaEntity.setPassagem(passagem);
        vendaEntity.setStatus(Status.CONCLUIDO);
        vendaEntity.setData(LocalDateTime.now());
        VendaEntity vendaEfetuada = vendaRepository.save(vendaEntity);
        logService.saveLog(companhiaEntity, VendaEntity.class, TipoOperacao.CRIAR);
        passagemService.alteraDisponibilidadePassagem(passagem, vendaEfetuada);

        VendaDTO vendaEfetuadaDTO = objectMapper.convertValue(vendaEfetuada, VendaDTO.class);
        vendaEfetuadaDTO.setComprador(compradorEntity.getNome());
        vendaEfetuadaDTO.setIdComprador(compradorEntity.getIdUsuario());
        vendaEfetuadaDTO.setCompanhia(companhiaEntity.getNome());

//        emailService.sendEmail(vendaEfetuada, "CRIAR", compradorEntity);

        return vendaEfetuadaDTO;
    }

    public boolean delete(Integer idVenda) throws RegraDeNegocioException {

        VendaEntity venda = vendaRepository.findById(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Venda não encontrada!"));

        if (venda.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Venda já cancelada!");
        }

        UsuarioDTO loggedUser = usuarioService.getLoggedUser();
        CompanhiaEntity companhiaVendedora = companhiaService.recuperarCompanhia("idPassagem", venda.getIdPassagem());

        if (!(loggedUser.getIdUsuario() == venda.getIdComprador() ||
                loggedUser.getIdUsuario() == companhiaVendedora.getIdUsuario())) {
            throw new RegraDeNegocioException("Você não tem permissão de cancelar essa venda!");
        }

        vendaRepository.deleteById(idVenda);
//        emailService.sendEmail(venda, "DELETAR",
//                compradorService.getCompradorComId(venda.getIdComprador()));
        logService.saveLog(companhiaVendedora, VendaEntity.class, TipoOperacao.DELETAR);
        return true;
    }

    public PageDTO<VendaDTO> getHistoricoComprasComprador(Integer idComprador,Integer pagina,
                                              Integer tamanho) throws RegraDeNegocioException {
        compradorService.getCompradorComId(idComprador);

        // Se o comprador logado não tiver o mesmo id que o fornecido e não for admin.
        // não tem permissão para acessar esse recurso
        if (!Objects.equals(idComprador, usuarioService.getIdLoggedUser()) &&
                usuarioService.getLoggedUser().getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RegraDeNegocioException("Você não tem permissão para realizar essa operação!");
        }

        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);

        return listaPaginada(vendaRepository.
                        findAllByIdComprador(idComprador, solcitacaoPagina),
                        pagina,
                        tamanho);
    }

    public PageDTO<VendaDTO> getHistoricoVendasCompanhia(Integer idCompanhia, Integer pagina,
                                             Integer tamanho) throws RegraDeNegocioException {

        companhiaService.getCompanhiaComId(idCompanhia);

        // Se a companhia logado não tiver o mesmo id que o fornecido e não for admin.
        // não tem permissão para acessar esse recurso
        if (!Objects.equals(idCompanhia, usuarioService.getIdLoggedUser()) &&
                usuarioService.getLoggedUser().getTipoUsuario() != TipoUsuario.ADMIN) {
            throw new RegraDeNegocioException("Você não tem permissão para realizar essa operação!");
        }

        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);

        return listaPaginada(vendaRepository.
                        findAllByIdCompanhia(idCompanhia, solcitacaoPagina),
                pagina,
                tamanho);
    }

    public PageDTO<VendaDTO> getVendasBetween(LocalDateTime inicio, LocalDateTime fim,
                                              Integer pagina, Integer tamanho) {
        Pageable solicitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<VendaDTO> vendasBetween = vendaRepository.findAllByDataBetween(inicio, fim,
                                                                        solicitacaoPagina);

        return listaPaginada(vendasBetween,
                            pagina,
                            tamanho);
    }

    protected PageDTO<VendaDTO> listaPaginada (Page<VendaDTO> pageVendaDTO, Integer pagina, Integer tamanho){

        return new PageDTO<>(pageVendaDTO.getTotalElements(),
                pageVendaDTO.getTotalPages(),
                pagina,
                tamanho,
                pageVendaDTO.getContent());
    }
}
