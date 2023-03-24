package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompanhiaDoc;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.CompanhiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/retornar-passagens")
    public  ResponseEntity<PageDTO<CompanhiaRelatorioDTO>> relatorioDePassagens(@RequestParam Integer pagina,
                                                                                @RequestParam Integer tamanho){
        return new ResponseEntity<>(companhiaService.companhiaRelatorio(pagina, tamanho), OK);
    }


    @GetMapping("/logar")
    public ResponseEntity<CompanhiaDTO> getByCompanhia() throws RegraDeNegocioException {
//        return new ResponseEntity<>(companhiaService.getLoginSenhaReturn(login,senha), OK);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<CompanhiaDTO> create(@Valid @RequestBody CompanhiaCreateDTO companhiaDTO)
            throws RegraDeNegocioException {
//        return new ResponseEntity<>(companhiaService.create(companhiaDTO), CREATED);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompanhiaDTO> update(@Valid @RequestBody CompanhiaUpdateDTO companhiaUpdateDTO)
                                                throws RegraDeNegocioException {
//        return new ResponseEntity<>(companhiaService.update(login, senha, companhiaUpdateDTO), OK);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestHeader("cnpj") String cnpj) throws RegraDeNegocioException {
//        companhiaService.delete(login, senha, cnpj);
        return ResponseEntity.noContent().build();
    }
}
