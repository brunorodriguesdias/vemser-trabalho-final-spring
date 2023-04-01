package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateAmountDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "Passagem", description = "Endpoints de passagem")
public interface PassagemDoc {

    @Operation(summary = "Criar passagem", description = "Cria uma nova passagem")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna a passagem criada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<PassagemDTO> create(@RequestBody @Valid PassagemCreateDTO passagemDTO)
            throws RegraDeNegocioException;

    @Operation(summary = "Criar passagem em quantidade", description = "Cria uma novas passagens")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna a passagens criadas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/amount")
    ResponseEntity<List<PassagemDTO>> createAmount(@RequestBody @Valid PassagemCreateAmountDTO passagemCreateAmountDTO) throws RegraDeNegocioException;

    @Operation(summary = "Editar passagem por id", description = "Edita os dados da passagem pelo id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os novos dados da passagem"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idPassagem}")
    ResponseEntity<PassagemDTO> update(@PathVariable("idPassagem") @Valid @Positive Integer id,
                                       @RequestBody @Valid PassagemCreateDTO passagemDTO)
            throws RegraDeNegocioException;

    @Operation(summary = "Deletar passagem por id", description = "Deleta a passagem selecionada")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idPassagem}")
    ResponseEntity<Void> delete(@PathVariable("idPassagem") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Listar ultimas passagens", description = "Lista as ultimas passagens cadastradas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/new")
    ResponseEntity<PageDTO<PassagemDTO>> getUltimasPassagens(@RequestParam Integer pagina, @RequestParam @Valid @Positive Integer tamanho);

    @Operation(summary = "Buscar passagens por id do voo", description = "Lista as passagens id do voo")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de passagens solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/voo")
    ResponseEntity<PageDTO<PassagemDTO>> getByVoo(@RequestParam("idVoo") @Valid @Positive Integer idVoo,
                                                  @RequestParam Integer pagina,
                                                  @RequestParam @Valid @Positive Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Buscar passagem por valor", description = "Lista as passagens até o limite de valor selecionado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de passagens do voo solicitado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/valor")
    ResponseEntity<PageDTO<PassagemDTO>> getByValorMaximo(@RequestParam("max") @Valid @Positive BigDecimal valor,
                                                       @RequestParam("pagina") Integer pagina,
                                                       @RequestParam("tamanho") @Valid @Positive Integer tamanho);

    @Operation(summary = "Buscar passagem por id", description = "Mostra os dados da passagem pelo id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a passagem solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idPassagem}")
    ResponseEntity<PassagemDTO> getById(@PathVariable("idPassagem") @Valid @Positive Integer id) throws RegraDeNegocioException;
}
