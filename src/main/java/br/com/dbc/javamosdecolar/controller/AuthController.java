package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.docs.AuthDoc;
import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.LoginDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.secutiry.AuthenticationService;
import br.com.dbc.javamosdecolar.secutiry.TokenService;
import br.com.dbc.javamosdecolar.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements AuthDoc {

    private final TokenService tokenService;
    private final UsuarioService usuarioService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public String auth(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException {
        return tokenService.gerarToken(authenticationService.autenticar(loginDTO));
    }

    @PostMapping("/create")
    public ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.create(usuarioCreateDTO), HttpStatus.OK);
    }

    @GetMapping("/get-usuario-loggado")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }
}
