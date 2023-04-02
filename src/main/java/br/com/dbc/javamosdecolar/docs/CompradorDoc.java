package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Tag(name = "Comprador", description = "Endpoints de comprador")
public interface CompradorDoc {

    @Operation(summary = "Listar compradores", description = "Lista todos os compradores cadastrados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de compradores cadastrados"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<CompradorDTO>> getAll(@RequestParam @Valid @PositiveOrZero(message = "Tamanho tem que ser igual ou maior que 0!") Integer pagina,
                                                 @RequestParam @Valid @Positive(message = "Tamanho tem que ser maior que 0!") Integer tamanho);

    @Operation(summary = "Logar na conta comprador", description = "Mostra os dados do comprador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o comprador solicitado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/logar")
    ResponseEntity<CompradorDTO> getByComprador() throws RegraDeNegocioException;

    @Operation(summary = "Relatório paginado", description = "Exibe relatório do comprador e suas compras.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o relatório na quantidade solicitada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/retornar-compras")
    ResponseEntity<PageDTO<CompradorRelatorioDTO>> relatorioDeCompras(@RequestParam @Valid @PositiveOrZero(message = "Tamanho tem que ser igual ou maior que 0!") Integer pagina,
                                                                      @RequestParam @Valid @Positive(message = "Tamanho tem que ser maior que 0!") Integer tamanho);


    @Operation(summary = "Criar comprador", description = "Cria um novo comprador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o comprador criado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<CompradorDTO> create(@Valid @RequestBody CompradorCreateDTO comprador)
            throws RegraDeNegocioException;

    @Operation(summary = "Editar comprador", description = "Edita os dados do comprador")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna os novos dados do comprador"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/alterar")
    ResponseEntity<CompradorDTO> update(@Valid
                                        @NotBlank(message = "É necessário informar uma senha!")
                                        @Size(min=3, max=20, message = "A senha deve ter entre 3 à 20 caracteres!")
                                        @RequestHeader String novaSenha)
            throws RegraDeNegocioException;

    @Operation(summary = "Deletar comprador", description = "Deleta o comprador autenticado")
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

    @Operation(summary = "Deletar comprador por id", description = "Deleta a comprador pelo id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/deletar/admin")
    ResponseEntity<Void> deleteCompradorAdmin(@RequestParam("Id") @Valid @Positive Integer id,
                                              @RequestHeader("cpf") @Valid @Size(min = 11, max = 11) @CPF String cpf) throws RegraDeNegocioException;
}
