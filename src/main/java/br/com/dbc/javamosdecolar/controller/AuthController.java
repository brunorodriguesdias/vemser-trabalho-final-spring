package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@RestController
//@RequestMapping("/auth")
//@Validated
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final TokenService tokenService;
//
//    private final UsuarioService usuarioService;
//
//    @PostMapping
//    public String auth(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException {
//        return tokenService.gerarToken(usuarioService.autenticar(loginDTO));
//    }
//
//    @PostMapping("/create")
//    public ResponseEntity<LoginDTO> create(@RequestBody @Valid LoginCreateDTO loginCreateDTO) {
//        return new ResponseEntity<>(usuarioService.create(loginCreateDTO), HttpStatus.OK);
//    }
//
//    @GetMapping("/get-usuario-loggado")
//    public ResponseEntity<LoginDTO> getUsuarioLogado() throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
//    }
//}
