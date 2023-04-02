package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.PassagemDoc;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateAmountDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.PassagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Validated
@RestController()
@RequestMapping("/passagem")
@RequiredArgsConstructor
public class PassagemController implements PassagemDoc {
    private final PassagemService passagemService;

    @PostMapping("/create")
    public ResponseEntity<PassagemDTO> create(PassagemCreateDTO passagemDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(this.passagemService.create(passagemDTO), CREATED);
    }

    @PostMapping("/amount")
    public ResponseEntity<List<PassagemDTO>> createAmount(PassagemCreateAmountDTO passagemCreateAmountDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.passagemService.createAmount(passagemCreateAmountDTO), CREATED);
    }

    @PutMapping("/{idPassagem}")
    public ResponseEntity<PassagemDTO> update(Integer id, PassagemCreateDTO passagemDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(this.passagemService.update(id, passagemDTO), OK);
    }

    @DeleteMapping("/{idPassagem}")
    public ResponseEntity<Void> delete(Integer id) throws RegraDeNegocioException {
        this.passagemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all-new")
    public ResponseEntity<PageDTO<PassagemDTO>> getUltimasPassagens(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(this.passagemService.getUltimasPassagens(pagina, tamanho), OK);
    }

    @GetMapping("/voo")
    public ResponseEntity<PageDTO<PassagemDTO>> getByVoo(Integer idVoo, Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.passagemService.getByVoo(idVoo, pagina, tamanho), OK);
    }

    @GetMapping("/valor")
    public ResponseEntity<PageDTO<PassagemDTO>> getByValorMaximo(BigDecimal valor, Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(this.passagemService.getByValorMaximo(valor, pagina, tamanho), OK);
    }

    @GetMapping("/buscar/{idPassagem}")
    public ResponseEntity<PassagemDTO> getById(Integer id) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.passagemService.getById(id), OK);
    }
}
