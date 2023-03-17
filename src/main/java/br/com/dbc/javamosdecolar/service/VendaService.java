package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.VendaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.model.*;
import br.com.dbc.javamosdecolar.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

            PassagemEntity passagem = objectMapper
                    .convertValue(passagemService.getById(vendaDTO.getIdPassagem()), PassagemEntity.class);

            if(!passagem.isDisponivel()) {
                throw new RegraDeNegocioException("Passagem indisponível.");
            }

            CompradorEntity comprador = objectMapper.convertValue(compradorService.getById(vendaDTO.getIdComprador()),
                    CompradorEntity.class);

            if(!comprador.isAtivo()) {
                throw new RegraDeNegocioException("Comprador indisponível.");
            }

            CompanhiaEntity companhiaEntity = objectMapper.convertValue(companhiaService.getById(vendaDTO.getIdCompanhia()),
                    CompanhiaEntity.class);

            if(!companhiaEntity.isAtivo()) {
                throw new RegraDeNegocioException("Companhia indisponível.");
            }

            VendaEntity vendaEntity = objectMapper.convertValue(vendaDTO, VendaEntity.class);
            vendaEntity.setCodigo(String.valueOf(codigo));
            VendaEntity vendaEfetuada = vendaRepository.save(vendaEntity);

            if(vendaEfetuada.equals(null)) {
                throw new RegraDeNegocioException("Não foi possível concluir a venda.");
            }

            boolean conseguiuEditar = passagemService.alteraDisponibilidadePassagem(passagem, vendaEfetuada);

            if(!conseguiuEditar) {
                throw new RegraDeNegocioException("Não foi possível concluir a venda.");
            }

            VendaDTO vendaEfetuadaDTO = objectMapper.convertValue(vendaEfetuada, VendaDTO.class);
            vendaEfetuadaDTO.setIdCompanhia(vendaDTO.getIdCompanhia());
            vendaEfetuadaDTO.setIdPassagem(vendaDTO.getIdPassagem());
            vendaEfetuadaDTO.setIdComprador(vendaDTO.getIdComprador());

            emailService.sendEmail(vendaEfetuada, "CRIAR", comprador);

            return vendaEfetuadaDTO;
    }

    public boolean delete(Integer idVenda) throws RegraDeNegocioException {

        VendaEntity venda = vendaRepository.findById(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Venda não encontrada!"));

        if (venda.getStatus().getTipo() == 2) {
            throw new RegraDeNegocioException("Venda já cancelada!");
        }

        vendaRepository.deleteById(idVenda);
        return true;
    }

//    public List<VendaDTO> getHistoricoComprasComprador(Integer idComprador) throws RegraDeNegocioException {
//
//            CompradorEntity compradorEntity = objectMapper.convertValue(compradorService.getById(idComprador), CompradorEntity.class);
//            return vendaRepository.getAllByComprador(compradorEntity)
//                    .stream()
//                    .map(venda -> {
//                        VendaDTO vendaDTO = objectMapper.convertValue(venda, VendaDTO.class);
//                        vendaDTO.setIdComprador(venda.);
//                        vendaDTO.setIdPassagem(venda.getPassagem().getIdPassagem());
//                        vendaDTO.setIdCompanhia(venda.getPassagem().getTrecho().getCompanhiaEntity().getIdCompanhia());
//
//                        return vendaDTO;
//                    }).toList();
//
//    }

//    public List<VendaDTO> getHistoricoVendasCompanhia(Integer id) throws RegraDeNegocioException {
//
//        companhiaService.getById(id);
//        return vendaRepository.getByCompanhia(id).stream()
//                .map(venda -> {
//                    VendaDTO vendaDTO = mapper.convertValue(venda, VendaDTO.class);
//                    vendaDTO.setIdComprador(venda.getComprador().getIdComprador());
//                    vendaDTO.setIdPassagem(venda.getPassagem().getIdPassagem());
//                    vendaDTO.setIdCompanhia(venda.getPassagem().getTrecho().getCompanhiaEntity().getIdCompanhia());
//
//                    return vendaDTO;
//                }).toList();
//    }
}
