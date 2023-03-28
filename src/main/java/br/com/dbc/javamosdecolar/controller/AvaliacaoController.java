package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @GetMapping("/all")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAll(@RequestParam Integer pagina,
                                                         @RequestParam Integer tamanho) {
        return new ResponseEntity(avaliacaoService.findAll(), OK);
    }

    @GetMapping("/listar-nota")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNota(@RequestParam Integer nota,
                                                               @RequestParam Integer pagina,
                                                               @RequestParam Integer tamanho) {
        return new ResponseEntity<>(avaliacaoService.findAllByNota(nota), HttpStatus.OK);
    }

    @GetMapping("/buscar-id")
    public ResponseEntity<AvaliacaoDTO> findByIdAvaliacao(@RequestParam Integer idAvaliacao) {
        return new ResponseEntity<>(avaliacaoService.findByIdAvaliacao(idAvaliacao), OK);
    }

    @PostMapping("/create")
    public ResponseEntity<AvaliacaoDTO> create (AvaliacaoCreateDTO avaliacaoCreateDTO) {
        return new ResponseEntity<>(avaliacaoService.create(avaliacaoCreateDTO));
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestParam Integer idAvaliacao) throws RegraDeNegocioException {
        avaliacaoService.delete(idAvaliacao);
        return ResponseEntity.noContent().build();
    }
}