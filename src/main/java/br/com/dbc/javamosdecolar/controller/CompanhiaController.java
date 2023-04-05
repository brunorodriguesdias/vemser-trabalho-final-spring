package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompanhiaDoc;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.LogDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.CompanhiaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/companhia")
public class CompanhiaController implements CompanhiaDoc{
    private final CompanhiaService companhiaService;

    @GetMapping("/listar-todas")
    public ResponseEntity<PageDTO<CompanhiaDTO>> getAll(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/retornar-passagens")
    public  ResponseEntity<PageDTO<CompanhiaRelatorioDTO>> relatorioDePassagens(Integer pagina,Integer tamanho) {
        return new ResponseEntity<>(companhiaService.gerarCompanhiaRelatorio(pagina, tamanho), OK);
    }

    @GetMapping("/buscar-companhia-logada")
    public ResponseEntity<CompanhiaDTO> getByCompanhia() throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.getLoggedCompanhia(), OK);
    }

    @PostMapping("/criar")
    public ResponseEntity<CompanhiaDTO> create(CompanhiaCreateDTO companhiaDTO) throws RegraDeNegocioException, JsonProcessingException {
        return new ResponseEntity<>(companhiaService.create(companhiaDTO), CREATED);
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompanhiaDTO> update(CompanhiaUpdateDTO companhiaUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.update(companhiaUpdateDTO), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete() throws RegraDeNegocioException {
        companhiaService.delete();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar/admin")
    public ResponseEntity<Void> deleteCompanhiaAdmin(Integer id, String cnpj) throws RegraDeNegocioException {
        companhiaService.delete(id, cnpj);
        return ResponseEntity.noContent().build();
    }
}
