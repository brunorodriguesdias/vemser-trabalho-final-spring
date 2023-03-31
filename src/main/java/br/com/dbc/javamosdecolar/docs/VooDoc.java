package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Tag(name = "Voo", description = "Endpoints dos voos")
public interface VooDoc {

    @Operation(summary = "Buscar um voo", description = "Buscar um voo pelo ID")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo buscado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/id")
    ResponseEntity<VooDTO> getById(@RequestParam @Valid @NotNull(message = "Digite um ID") Integer idVoo) throws RegraDeNegocioException;

    @Operation(summary = "Buscar um voo pelo avião", description = "Buscar um voo pelo ID avião")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo buscado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/voo-aviao")
    ResponseEntity<PageDTO<VooDTO>> getByVooAviao(@RequestParam("idAviao") Integer idAviao,
                                                         @RequestParam("pagina") Integer pagina,
                                                         @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Buscar um voo pela companhia", description = "Buscar um voo pelo ID companhia")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo buscado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/voo-companhia")
    ResponseEntity<PageDTO<VooDTO>> getByVooCompanhia(@RequestParam("idCompanhia") Integer idCompanhia,
                                                             @RequestParam("pagina") Integer pagina,
                                                             @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Buscar todos os voos", description = "Buscar um voô pelo ID voo")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo buscado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/voo-all")
    ResponseEntity<PageDTO<VooDTO>> getAllVoo( @RequestParam("pagina") Integer pagina,
                                                      @RequestParam("tamanho") Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Criar um voo", description = "Cria um novo voo")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo criado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    public ResponseEntity<VooDTO> create(@RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException;


    @Operation(summary = "Editar um voo", description = "Edita o voô informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna o voo editado"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idVoo}")
    public ResponseEntity<VooDTO> update (@PathVariable("idVoo") Integer idVoo,
                                          @RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException;


    @Operation(summary = "Cancelar voo", description = "Cancela o voo informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Retorna que a operação foi concluida"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping
    ResponseEntity<Void> delete(@PathVariable("idVoo") Integer idVoo) throws RegraDeNegocioException;
}
