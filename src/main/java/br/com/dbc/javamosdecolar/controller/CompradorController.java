package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompradorDoc;
import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.CompradorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/comprador")
@AllArgsConstructor
public class CompradorController implements CompradorDoc {

    private final CompradorService compradorService;

    @GetMapping("/all")
    public ResponseEntity<PageDTO<CompradorDTO>> getAll(@RequestParam Integer pagina,
                                                        @RequestParam Integer tamanho) {
        return new ResponseEntity<>(compradorService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/logar")
    public ResponseEntity<CompradorDTO> getByComprador() throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.getByComprador(), OK);
    }

    @GetMapping("/retornar-compras")
    public  ResponseEntity<PageDTO<CompradorRelatorioDTO>> relatorioDeCompras(@RequestParam Integer pagina,
                                                                              @RequestParam Integer tamanho){
        return new ResponseEntity<>(compradorService.compradorComComprasRelatorio(pagina, tamanho), OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CompradorDTO> create(@Valid @RequestBody CompradorCreateDTO comprador)
                                                throws RegraDeNegocioException{
        return new ResponseEntity<>(compradorService.create(comprador), CREATED);
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompradorDTO> update(@Valid
                                               @NotBlank(message = "É necessário informar uma senha!")
                                               @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!") @RequestHeader String novaSenha)
                                                throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.update(novaSenha), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete() throws RegraDeNegocioException {
        compradorService.delete();
        return ResponseEntity.noContent().build();
    }
}

