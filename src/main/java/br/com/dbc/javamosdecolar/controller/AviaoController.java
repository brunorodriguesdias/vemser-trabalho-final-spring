package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.AviaoDoc;
import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.AviaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@RequestMapping("/aviao")
@RequiredArgsConstructor
public class AviaoController implements AviaoDoc {
    private final AviaoService aviaoService;

    @GetMapping("/all")
    public ResponseEntity<PageDTO<AviaoDTO>> getAll(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(aviaoService.getAll(pagina, tamanho), OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AviaoDTO> create(AviaoCreateDTO aviao)
            throws RegraDeNegocioException{
        return new ResponseEntity<>(aviaoService.create(aviao), CREATED);
    }

    @PutMapping("/{idAviao}")
    public ResponseEntity<AviaoDTO> update(Integer idAviao, AviaoCreateDTO aviao) throws RegraDeNegocioException{
        return new ResponseEntity<>(aviaoService.update(idAviao, aviao), OK);
    }

    @DeleteMapping("/deletar/{idAviao}")
    public ResponseEntity<Void> delete(Integer idAviao) throws RegraDeNegocioException {
        aviaoService.delete(idAviao);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<AviaoDTO> getById(Integer idAviao) throws RegraDeNegocioException {
        return new ResponseEntity<>(aviaoService.getById(idAviao), OK);
    }

}
