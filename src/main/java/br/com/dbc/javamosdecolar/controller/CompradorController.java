package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompradorDoc;
import br.com.dbc.javamosdecolar.dto.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.PageDTO;
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

    @GetMapping
    public ResponseEntity<PageDTO<CompradorDTO>> getAll(@RequestParam Integer pagina,
                                                        @RequestParam Integer tamanho) {
        return new ResponseEntity<>(compradorService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/logar")
    public ResponseEntity<CompradorDTO> getByLoginSenha(@Valid @RequestHeader("login") String login,
                                                @Valid @RequestHeader("senha") String senha) throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.getLoginSenhaReturn(login, senha), OK);
    }

    @GetMapping("/retornar-compras")
    public  ResponseEntity<PageDTO<CompradorRelatorioDTO>> relatorioDeCompras(@RequestParam Integer pagina,
                                                                              @RequestParam Integer tamanho){
        return new ResponseEntity<>(compradorService.compradorRelatorio(pagina, tamanho), OK);
    }

    @PostMapping
    public ResponseEntity<CompradorDTO> create(@Valid @RequestBody CompradorCreateDTO comprador)
                                                throws RegraDeNegocioException{
        return new ResponseEntity<>(compradorService.create(comprador), CREATED);
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompradorDTO> update(@RequestHeader("login") String login,
                                               @RequestHeader("senha") String senha,
                                               @Valid
                                               @NotBlank(message = "É necessário informar uma senha!")
                                               @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!") @RequestHeader String novaSenha)
                                                throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.update(login, senha, novaSenha), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestHeader("login") String login,
                                       @RequestHeader("senha") String senha,
                                       @RequestHeader("cpf") String cpf) throws RegraDeNegocioException {
        compradorService.delete(login,senha,cpf);
        return ResponseEntity.noContent().build();
    }
}

