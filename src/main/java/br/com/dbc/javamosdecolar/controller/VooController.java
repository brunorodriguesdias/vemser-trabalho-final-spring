package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.VooDoc;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.VooService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/voo")
@RequiredArgsConstructor
public class VooController implements VooDoc {
    private final VooService vooService;

    @GetMapping("/id")
    public ResponseEntity<VooDTO> getById(@RequestParam("idVoo") Integer idVoo) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.getById(idVoo), OK);
    }

    @GetMapping("/aviao")
    public ResponseEntity<PageDTO<VooDTO>> getByVooAviao(@RequestParam("idAviao") Integer idAviao,
                                                        @RequestParam("pagina") Integer pagina,
                                                        @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.getByVooAviao(idAviao, pagina, tamanho), OK);
    }

    @GetMapping("/companhia")
    public ResponseEntity<PageDTO<VooDTO>> getByVooCompanhia(@RequestParam("idCompanhia") Integer idCompanhia,
                                                         @RequestParam("pagina") Integer pagina,
                                                         @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.getByVooCompanhia(idCompanhia, pagina, tamanho), OK);
    }

    @GetMapping("/all")
    public ResponseEntity<PageDTO<VooDTO>> getAllVoo( @RequestParam("pagina") Integer pagina,
                                                             @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.getAllVoo(pagina, tamanho), OK);
    }

    @PostMapping("/criar")
    public ResponseEntity<VooDTO> create(@RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.create(vooCreateDTO), CREATED);
    }

    @PutMapping("/alterar/{idVoo}")
    public ResponseEntity<VooDTO> update (@PathVariable("idVoo") Integer idVoo,
                                          @RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(vooService.update(idVoo, vooCreateDTO), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestParam("idVoo") Integer idVoo) throws RegraDeNegocioException {
        vooService.delete(idVoo);
        return new ResponseEntity<>(OK);
    }
}
