package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.PageDTO;
import br.com.dbc.javamosdecolar.dto.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.VendaDTO;
import br.com.dbc.javamosdecolar.entity.*;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;
    private final PassagemService passagemService;
    private final CompradorService compradorService;
    private final CompanhiaService companhiaService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public VendaDTO create(VendaCreateDTO vendaDTO) throws RegraDeNegocioException {


            UUID codigo = UUID.randomUUID();

            PassagemEntity passagem = passagemService.getPassagem(vendaDTO.getIdPassagem());
            if (passagem.getStatus() != Status.DISPONIVEL) {
                throw new RegraDeNegocioException("Passagem indisponível!");
            }

            CompradorEntity compradorEntity = compradorService.getComprador(vendaDTO.getIdComprador());

            CompanhiaEntity companhiaEntity = companhiaService.getCompanhia(vendaDTO.getIdCompanhia());

            VendaEntity vendaEntity = objectMapper.convertValue(vendaDTO, VendaEntity.class);
            vendaEntity.setCodigo(String.valueOf(codigo));
            vendaEntity.setCompanhia(companhiaEntity);
            vendaEntity.setComprador(compradorEntity);
            vendaEntity.setPassagem(passagem);
            vendaEntity.setStatus(Status.CONCLUIDO);
            vendaEntity.setData(LocalDateTime.now());
            VendaEntity vendaEfetuada = vendaRepository.save(vendaEntity);

            if(vendaEfetuada.equals(null)) {
                throw new RegraDeNegocioException("Não foi possível concluir a venda.");
            }

            passagemService.alteraDisponibilidadePassagem(passagem, vendaEfetuada);

            VendaDTO vendaEfetuadaDTO = objectMapper.convertValue(vendaEfetuada, VendaDTO.class);
//            vendaEfetuadaDTO.setIdCompanhia(vendaEfetuada.getCompanhia().getIdUsuario());
//            vendaEfetuadaDTO.setIdPassagem(passagem.getIdPassagem());
//            vendaEfetuadaDTO.setIdComprador(vendaEfetuada.getComprador().getIdUsuario());

//            emailService.sendEmail(vendaEfetuada, "CRIAR", compradorEntity);

            return vendaEfetuadaDTO;
    }

    public boolean delete(Integer idVenda) throws RegraDeNegocioException {

        VendaEntity venda = vendaRepository.findById(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Venda não encontrada!"));

        if (venda.getStatus() == Status.CANCELADO) {
            throw new RegraDeNegocioException("Venda já cancelada!");
        }

        vendaRepository.deleteById(idVenda);
        return true;
    }

    public List<VendaDTO> getHistoricoComprasComprador(Integer idComprador) throws RegraDeNegocioException {

            CompradorEntity compradorEntity = compradorService.getComprador(idComprador);
            List<VendaDTO> vendaDTOList = vendaRepository.findAllByIdComprador(idComprador)
                    .stream()
                    .map(venda -> {
                        VendaDTO vendaDTO = objectMapper.convertValue(venda, VendaDTO.class);
                        return vendaDTO;
                    }).toList();
            return vendaDTOList;
    }

    public List<VendaDTO> getHistoricoVendasCompanhia(Integer idCompanhia) throws RegraDeNegocioException {

        CompanhiaEntity companhiaEntity = companhiaService.getCompanhia(idCompanhia);

        List<VendaDTO> vendaDTOList =  vendaRepository.findAllByIdCompanhia(idCompanhia)
                .stream()
                .map(venda -> {
                    VendaDTO vendaDTO = objectMapper.convertValue(venda, VendaDTO.class);
                    return vendaDTO;
                }).toList();
        return vendaDTOList;
    }

    public PageDTO<VendaDTO> getVendasBetween(LocalDateTime inicio, LocalDateTime fim, Integer pagina, Integer tamanho) {
        Pageable solicitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<VendaEntity> vendasBetween = vendaRepository.findAllByDataBetween(solicitacaoPagina, inicio, fim);

        List<VendaDTO> paginaDeVendasDTO = vendasBetween.getContent().stream()
                .map(pessoas -> objectMapper.convertValue(pessoas, VendaDTO.class))
                .toList();

        return new PageDTO<>(vendasBetween.getTotalElements(),
                vendasBetween.getTotalPages(),
                pagina,
                tamanho,
                paginaDeVendasDTO);
    }
}
