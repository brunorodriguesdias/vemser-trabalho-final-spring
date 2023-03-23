package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Avião", description = "Endpoints de avião")
public interface AviaoDoc {
    @Operation(summary = "Lista todos os aviãos", description = "Lista todos os aviãos cadastrados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os aviãos cadastrados"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<AviaoDTO>> getAll(@RequestParam Integer pagina, @RequestParam Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Cria um avião", description = "Cadastra um novo avião")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna a lista solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<AviaoDTO> create(@Valid @RequestBody AviaoCreateDTO avião) throws RegraDeNegocioException;

    @Operation(summary = "Atualiza um avião", description = "Atualiza um avião cadastrado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idAviao}")
    ResponseEntity<AviaoDTO> update(@PathVariable("idAviao") Integer idAviao,
                                     @Valid @RequestBody AviaoCreateDTO avião) throws RegraDeNegocioException;

    @Operation(summary = "Deleta um avião", description = "Remove o avião da base de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idAviao}")
    ResponseEntity<Void> delete(@PathVariable("idAviao") Integer idAviao) throws RegraDeNegocioException;

    @Operation(summary = "Retorna um avião específico", description = "Retorna um avião a partir de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idAviao}")
    ResponseEntity<AviaoDTO> getById(@PathVariable("idAviao") Integer idAviao)
            throws RegraDeNegocioException;

//    @Operation(summary = "Retorna aviãos de uma companhia", description = "Lista todos os aviãos cadastrados" +
//            " pertencentes a uma companhia")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "Retorna a lista solicitada"),
//                    @ApiResponse(responseCode = "400", description = "Bad Request"),
//                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
//                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
//            }
//    )
//    @GetMapping("/{idCompanhia}")
//    ResponseEntity<PageDTO<AviaoDTO>> getByCompanhia(@PathVariable("idCompanhia") Integer idCompanhia)
//            throws RegraDeNegocioException;
}
