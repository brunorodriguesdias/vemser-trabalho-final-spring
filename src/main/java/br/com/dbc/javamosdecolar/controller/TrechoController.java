package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.TrechoDoc;
import br.com.dbc.javamosdecolar.dto.in.TrechoCreateDTO;
import br.com.dbc.javamosdecolar.dto.out.PageDTO;
import br.com.dbc.javamosdecolar.dto.out.TrechoDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.TrechoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/trecho")
@AllArgsConstructor
public class TrechoController implements TrechoDoc {

    private final TrechoService trechoService;

    @PostMapping
    public ResponseEntity<TrechoDTO> create(@Valid @RequestBody TrechoCreateDTO trecho)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(trechoService.create(trecho), CREATED);
    }

    @PutMapping("/{idTrecho}")
    public ResponseEntity<TrechoDTO> update(@PathVariable("idTrecho") Integer idTrecho, @Valid @RequestBody TrechoCreateDTO trecho)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(trechoService.update(idTrecho, trecho), OK);
    }

    @DeleteMapping("/{idTrecho}")
    public ResponseEntity<TrechoDTO> delete(@PathVariable("idTrecho") Integer idTrecho)
            throws RegraDeNegocioException {
        trechoService.delete(idTrecho);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<TrechoDTO>> getAll(@RequestParam Integer pagina,
                                                     @RequestParam Integer tamanho) {
        return new ResponseEntity<>(trechoService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/{idTrecho}")
    public ResponseEntity<TrechoDTO> getById(@PathVariable("idTrecho") Integer idTrecho)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(trechoService.getById(idTrecho), OK);
    }

//    @GetMapping("/{idCompanhia}/companhia")
//    public ResponseEntity<List<TrechoDTO>> getByCompanhia(@PathVariable("idCompanhia") Integer idCompanhia)
//            throws RegraDeNegocioException {
//        return new ResponseEntity<>(trechoService.getByCompanhia(idCompanhia), OK);
//    }
}
