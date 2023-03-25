package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.LoginDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public interface AuthDoc {

    @Operation(summary = "Rota de autenticação", description = "Acesse esse recurso para recuperar seu token")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Um novo token foi gerado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    String auth(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException;

    @Operation(summary = "Criar usuário", description = "Cria um usuário administrador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Um novo token foi gerado"),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar essa rota"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create")
    ResponseEntity<UsuarioDTO> create(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO)
            throws RegraDeNegocioException;

    @Operation(summary = "Visualizar usuário logado", description = "Retorna os dados do usuário autenticado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Um novo token foi gerado"),
                    @ApiResponse(responseCode = "403", description = "Você não possui permissão para acessar essa rota"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/get-usuario-loggado")
    ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException;
}
