package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.AvaliacaoDoc;
import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoRelatorioDTO;
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
public class AvaliacaoController implements AvaliacaoDoc {

    private final AvaliacaoService avaliacaoService;
    @Override
    @GetMapping("/all")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAll(Integer pagina, Integer tamanho) {
        return new ResponseEntity(avaliacaoService.findAll(pagina, tamanho), OK);
    }

    @Override
    @GetMapping("/listar-nota")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNota(Integer nota, Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(avaliacaoService.findAllByNota(pagina, tamanho, nota), HttpStatus.OK);
    }

    @Override
    @GetMapping("/listar-nome")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNome(String nome, Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(avaliacaoService.findAllByNome(pagina, tamanho, nome), HttpStatus.OK);
    }

    @Override
    @GetMapping("/buscar-id")
    public ResponseEntity<AvaliacaoDTO> findByIdAvaliacao(String idAvaliacao) throws RegraDeNegocioException {
        return new ResponseEntity<>(avaliacaoService.findByIdAvaliacao(idAvaliacao), OK);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<AvaliacaoRelatorioDTO> findByIdAvaliacao() {
        return new ResponseEntity<>(avaliacaoService.gerarRelatorio(), OK);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<AvaliacaoDTO> create (AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(avaliacaoService.create(avaliacaoCreateDTO), OK);
    }

    @Override
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(String idAvaliacao) throws RegraDeNegocioException {
        avaliacaoService.delete(idAvaliacao);
        return ResponseEntity.noContent().build();
    }
}
