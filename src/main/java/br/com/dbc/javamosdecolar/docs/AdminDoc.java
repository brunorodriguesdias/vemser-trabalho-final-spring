package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.*;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface AdminDoc {
    @Operation(summary = "Buscar um voo", description = "Buscar um voo pelo ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo buscado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/buscar-log/{pagina}/{tamanho}")
    public ResponseEntity<PageDTO<LogDTO>> consultLogsUsuario(@RequestParam(value = "idUsuario", required = false) Integer idUsuario,
                                                              @PathVariable("pagina") Integer pagina,
                                                              @PathVariable("tamanho") Integer tamanho);
    @Operation(summary = "Criar um avião", description = "Cria um avião e atribui a uma companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o avião criado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create-aviao/{idCompanhia}")
    public ResponseEntity<AviaoDTO> createAviao(@Valid @RequestBody AviaoCreateDTO aviaoCreateDTO);
    @Operation(summary = "Criar uma passagem", description = "Buscar um voo pelo ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Cria uma passagem e atribui a uma companhia"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create-passagem/{idCompanhia}")
    public ResponseEntity<PassagemDTO> createPassagem(@RequestBody @Valid PassagemCreateDTO passagemCreateDTO);
    @Operation(summary = "Criar um voo", description = "Cria um voo e atribui a uma companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo criado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create-voo/{idCompanhia}")
    public ResponseEntity<VooDTO> createVoo(@RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException;
}
