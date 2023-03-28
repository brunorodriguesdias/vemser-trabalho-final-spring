package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avaliacao")
@Validated
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping("/all")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAll(@RequestParam Integer pagina,
                                                         @RequestParam @Valid @Positive Integer tamanho) {
        return new ResponseEntity(avaliacaoService.findAll(pagina, tamanho), OK);
    }

    @GetMapping("/listar-nota")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNota(@RequestParam Integer nota,
                                                               @RequestParam Integer pagina,
                                                               @RequestParam @Valid @Positive Integer tamanho) {
        return new ResponseEntity<>(avaliacaoService.findAllByNota(pagina, tamanho, nota), HttpStatus.OK);
    }

    @GetMapping("/buscar-id")
    public ResponseEntity<AvaliacaoDTO> findByIdAvaliacao(@RequestParam String idAvaliacao) throws RegraDeNegocioException {
        return new ResponseEntity<>(avaliacaoService.findByIdAvaliacao(idAvaliacao), OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AvaliacaoDTO> create (AvaliacaoCreateDTO avaliacaoCreateDTO) {
        return new ResponseEntity<>(avaliacaoService.create(avaliacaoCreateDTO), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestParam String idAvaliacao) throws RegraDeNegocioException {
        avaliacaoService.delete(idAvaliacao);
        return ResponseEntity.noContent().build();
    }
}
