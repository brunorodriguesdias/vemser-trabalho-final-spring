package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompanhiaDoc;
import br.com.dbc.javamosdecolar.dto.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.CompanhiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/companhia")
public class CompanhiaController implements CompanhiaDoc{
    private final CompanhiaService companhiaService;

    @GetMapping
    public ResponseEntity<PageDTO<CompanhiaDTO>> getAll(@RequestParam Integer pagina,
                                                        @RequestParam Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/logar")
    public ResponseEntity<CompanhiaDTO> getByLoginSenha(@Valid @RequestHeader("login") String login,
                                                        @Valid @RequestHeader("senha") String senha) throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.getLoginSenhaReturn(login,senha), OK);
    }

    @PostMapping
    public ResponseEntity<CompanhiaDTO> create(@Valid @RequestBody CompanhiaCreateDTO companhiaDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.create(companhiaDTO), CREATED);
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompanhiaDTO> update(@RequestHeader("login") String login,
                                               @RequestHeader("senha") String senha,
                                               @Valid @RequestBody CompanhiaUpdateDTO companhiaUpdateDTO)
                                                throws RegraDeNegocioException {
        return new ResponseEntity<>(companhiaService.update(login, senha, companhiaUpdateDTO), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestHeader("login") String login,
                                       @RequestHeader("senha") String senha,
                                       @RequestHeader("cnpj") String cnpj) throws RegraDeNegocioException {
        companhiaService.delete(login, senha, cnpj);
        return ResponseEntity.noContent().build();
    }
}
