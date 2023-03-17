package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.VendaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.entity.*;
import br.com.dbc.javamosdecolar.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

            CompradorEntity compradorEntity = objectMapper.convertValue(compradorService.getById(vendaDTO.getIdComprador()),
                    CompradorEntity.class);

            CompanhiaEntity companhiaEntity = objectMapper.convertValue(companhiaService.getById(vendaDTO.getIdCompanhia()),
                    CompanhiaEntity.class);

            VendaEntity vendaEntity = objectMapper.convertValue(vendaDTO, VendaEntity.class);
            vendaEntity.setCodigo(String.valueOf(codigo));
            vendaEntity.setCompanhia(companhiaEntity);
            vendaEntity.setComprador(compradorEntity);
            vendaEntity.setIdPassagem(passagem.getIdPassagem());
            VendaEntity vendaEfetuada = vendaRepository.save(vendaEntity);

            if(vendaEfetuada.equals(null)) {
                throw new RegraDeNegocioException("Não foi possível concluir a venda.");
            }

            boolean conseguiuEditar = passagemService.alteraDisponibilidadePassagem(passagem, vendaEfetuada);

            if(!conseguiuEditar) {
                throw new RegraDeNegocioException("Não foi possível concluir a venda.");
            }

            VendaDTO vendaEfetuadaDTO = objectMapper.convertValue(vendaEfetuada, VendaDTO.class);
            vendaEfetuadaDTO.setIdCompanhia(vendaEfetuada.getCompanhia().getIdUsuario());
            vendaEfetuadaDTO.setIdPassagem(passagem.getIdPassagem());
            vendaEfetuadaDTO.setIdComprador(vendaEfetuada.getComprador().getIdUsuario());

//            emailService.sendEmail(vendaEfetuada, "CRIAR", comprador);

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

//    public List<VendaDTO> getHistoricoComprasComprador(Integer idUsuario) throws RegraDeNegocioException {
//
//            CompradorEntity compradorEntity = compradorService.getComprador(idUsuario);
//            return vendaRepository.getAllByComprador(compradorEntity)
//                    .stream()
//                    .map(venda -> {
//                        VendaDTO vendaDTO = objectMapper.convertValue(venda, VendaDTO.class);
//                        vendaDTO.setIdCompanhia(venda.getCompanhia().getIdUsuario());
//                        vendaDTO.setIdPassagem(venda.getPassagem().getIdPassagem());
//                        vendaDTO.setIdComprador(venda.getComprador().getIdUsuario());
//                        return vendaDTO;
//                    }).toList();
//    }

    public List<VendaDTO> getHistoricoVendasCompanhia(Integer idUsuario) throws RegraDeNegocioException {

        CompanhiaEntity companhiaEntity = companhiaService.getCompanhia(idUsuario);
        return vendaRepository.findAllByCompanhiaIdUsuarioAndStatusIsTrueOrStatusIsFalse(companhiaEntity)
                .stream()
                .map(venda -> {
                    VendaDTO vendaDTO = objectMapper.convertValue(venda, VendaDTO.class);
                    vendaDTO.setIdCompanhia(venda.getCompanhia().getIdUsuario());
                    vendaDTO.setIdPassagem(venda.getPassagem().getIdPassagem());
                    vendaDTO.setIdComprador(venda.getComprador().getIdUsuario());
                    return vendaDTO;
                }).toList();
    }
}
