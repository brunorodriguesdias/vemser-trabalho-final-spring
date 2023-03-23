package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.VooDoc;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.VooService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("/voo")
@RequiredArgsConstructor
public class VooController implements VooDoc {
    private final VooService vooService;

    @PostMapping
    public ResponseEntity<VooDTO> create(@RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.vooService.create(vooCreateDTO), CREATED);
    }

    @PutMapping("/{idVoo}")
    public ResponseEntity<VooDTO> update (@PathVariable("idVoo") Integer idVoo,
                                          @RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(vooService.update(idVoo, vooCreateDTO), OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable("idVoo") Integer idVoo) throws RegraDeNegocioException {
        vooService.delete(idVoo);
        return  ResponseEntity.noContent().build();
    }
}
