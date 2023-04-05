package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.VendaDoc;
import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VendaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.VendaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/venda")
@RequiredArgsConstructor
public class VendaController implements VendaDoc{

    private final VendaService vendaService;

    @Override
    @GetMapping("/{idComprador}/comprador")
    public ResponseEntity<PageDTO<VendaDTO>> getByHistoricoCompras(Integer id, Integer pagina, Integer tamanho)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(vendaService.getHistoricoComprasComprador(id, pagina, tamanho), OK);
    }
    @Override
    @GetMapping("/{idCompanhia}/companhia")
    public ResponseEntity<PageDTO<VendaDTO>> getByHistoricoVendas(Integer id, Integer pagina, Integer tamanho)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(vendaService.getHistoricoVendasCompanhia(id, pagina, tamanho), OK);
    }
    @Override
    @GetMapping("/vendas-between")
    public ResponseEntity<PageDTO<VendaDTO>> getVendasBetween(LocalDateTime inicioConsulta, LocalDateTime fimConsulta,
                                                           Integer paginaSolicitada, Integer tamanhoPagina) {
        return new ResponseEntity<>(vendaService.getVendasBetween(inicioConsulta,
                fimConsulta, paginaSolicitada, tamanhoPagina), OK);
    }
    @Override
    @PostMapping
    public ResponseEntity<VendaDTO> create(VendaCreateDTO vendaDTO) throws RegraDeNegocioException, JsonProcessingException {
        return new ResponseEntity<>(vendaService.create(vendaDTO), CREATED);
    }

    @Override
    @DeleteMapping("/{idVenda}/cancelar")
    public ResponseEntity<Void> delete(Integer idVenda) throws RegraDeNegocioException, JsonProcessingException {
        vendaService.delete(idVenda);
        return ResponseEntity.noContent().build();
    }
}
