package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.dto.outs.LogDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final LogService logService;

    @GetMapping("/buscar-log/{pagina}/{tamanho}")
    public ResponseEntity<PageDTO<LogDTO>> consultLogsUsuario(@RequestParam(value = "idUsuario", required = false) Integer idUsuario,
                                                              @PathVariable("pagina") Integer pagina,
                                                              @PathVariable("tamanho") Integer tamanho) {
        return new ResponseEntity<>(logService.consultLogsUsuario(idUsuario, pagina, tamanho), OK);
    }
}
