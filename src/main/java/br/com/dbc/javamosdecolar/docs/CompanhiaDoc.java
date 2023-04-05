package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Tag(name = "Companhia", description = "Endpoints de companhia")
public interface CompanhiaDoc {

    @Operation(summary = "Listar companhias", description = "Lista todas as companhias cadastradas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de companhias cadastradas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<CompanhiaDTO>> getAll(@RequestParam @Valid @PositiveOrZero(message = "Tamanho tem que ser igual ou maior que 0!") Integer pagina,
                                                 @RequestParam @Valid @Positive(message = "Tamanho tem que ser maior que 0!") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Listar companhias com passagens", description = "Listar companhias com passagens vendidas/disponíveis/canceladas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de passagens cadastradas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/retornar-passagens")
    ResponseEntity<PageDTO<CompanhiaRelatorioDTO>> relatorioDePassagens(@RequestParam @Valid @PositiveOrZero(message = "Tamanho tem que ser igual ou maior que 0!") Integer pagina,
                                                                        @RequestParam @Valid @Positive(message = "Tamanho tem que ser maior que 0!") Integer tamanho);

    @Operation(summary = "Buscar companhia", description = "Mostra os dados da companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a companhia solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/buscar-companhia")
    ResponseEntity<CompanhiaDTO> getByCompanhia() throws RegraDeNegocioException;

    @Operation(summary = "Criar companhia", description = "Cria uma nova companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna a companhia criada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<CompanhiaDTO> create(@Valid @RequestBody CompanhiaCreateDTO companhiaDTO) throws RegraDeNegocioException, JsonProcessingException;

    @Operation(summary = "Editar companhia", description = "Edita os dados da companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os novos dados da companhia"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/alterar")
    ResponseEntity<CompanhiaDTO> update(@Valid @RequestBody CompanhiaUpdateDTO companhiaUpdateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar companhia", description = "Deleta a companhia autenticada")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/deletar")
    ResponseEntity<Void> delete() throws RegraDeNegocioException;

    @Operation(summary = "Deletar companhia por id", description = "Deleta a companhia pelo id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/deletar/admin")
    ResponseEntity<Void> deleteCompanhiaAdmin(@RequestHeader("id") @Valid @PositiveOrZero(message = "Tamanho tem que ser igual ou maior que 0!") Integer id,
                                              @RequestHeader("cnpj") @Valid @CNPJ String cnpj) throws RegraDeNegocioException;
}
