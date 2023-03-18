package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.VendaDoc;
import br.com.dbc.javamosdecolar.dto.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.dto.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.VendaDTO;
import br.com.dbc.javamosdecolar.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Validated
@RestController
@RequestMapping("/venda")
@RequiredArgsConstructor
public class VendaController implements VendaDoc{

    private final VendaService vendaService;

    @Override
    @GetMapping("/{idComprador}/comprador")
    public ResponseEntity<List<VendaDTO>> getByHistoricoCompras(@PathVariable("idComprador") Integer id)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(vendaService.getHistoricoComprasComprador(id), OK);
    }
    @Override
    @GetMapping("/{idCompanhia}/companhia")
    public ResponseEntity<List<VendaDTO>> getByHistoricoVendas(@PathVariable("idCompanhia") Integer id)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(vendaService.getHistoricoVendasCompanhia(id), OK);
    }
    @GetMapping("/vendas-between")
    public ResponseEntity<PageDTO<VendaDTO>> getVendasBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicioConsulta,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fimConsulta,
                                                           @RequestParam Integer paginaSolicitada,
                                                           @RequestParam Integer tamanhoPagina) {
        return new ResponseEntity<>(vendaService.getVendasBetween(inicioConsulta,
                fimConsulta, paginaSolicitada, tamanhoPagina), OK);
    }
    @Override
    @PostMapping
    public ResponseEntity<VendaDTO> create(@RequestBody @Valid VendaCreateDTO vendaDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(vendaService.create(vendaDTO), CREATED);
    }

    @Override
    @DeleteMapping("/{idVenda}/cancelar")
    public ResponseEntity<Void> delete(@PathVariable("idVenda") Integer idVenda) throws RegraDeNegocioException {
        vendaService.delete(idVenda);
        return ResponseEntity.noContent().build();
    }
}
