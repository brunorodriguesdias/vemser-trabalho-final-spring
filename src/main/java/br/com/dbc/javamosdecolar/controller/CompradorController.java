package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.CompradorDoc;
import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.CompradorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController
@RequestMapping("/comprador")
@AllArgsConstructor
public class CompradorController implements CompradorDoc {

    private final CompradorService compradorService;

    @GetMapping("/listar-todos")
    public ResponseEntity<PageDTO<CompradorDTO>> getAll(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(compradorService.getAll(pagina, tamanho), OK);
    }

    @GetMapping("/buscar-comprador-logado")
    public ResponseEntity<CompradorDTO> getByComprador() throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.getByComprador(), OK);
    }

    @GetMapping("/retornar-compras")
    public  ResponseEntity<PageDTO<CompradorRelatorioDTO>> relatorioDeCompras(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(compradorService.gerarRelatorioCompras(pagina, tamanho), OK);
    }

    @PostMapping("/criar")
    public ResponseEntity<CompradorDTO> create(CompradorCreateDTO comprador) throws RegraDeNegocioException, JsonProcessingException {
        return new ResponseEntity<>(compradorService.create(comprador), CREATED);
    }

    @PutMapping("/alterar")
    public ResponseEntity<CompradorDTO> update(String novaSenha) throws RegraDeNegocioException {
        return new ResponseEntity<>(compradorService.update(novaSenha), OK);
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete() throws RegraDeNegocioException {
        compradorService.delete();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deletar/admin")
    public ResponseEntity<Void> deleteCompradorAdmin(Integer id, String cpf) throws RegraDeNegocioException {
        compradorService.delete(id, cpf);
        return ResponseEntity.noContent().build();
    }
}

