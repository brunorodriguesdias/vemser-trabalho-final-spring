package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.VendaDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Venda", description = "Endpoints de venda")
public interface VendaDoc {

    @Operation(summary = "Efetuar Venda", description = "Lista todas as companhias cadastradas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Venda realizada com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<VendaDTO> create(@RequestBody @Valid VendaCreateDTO vendaDTO) throws RegraDeNegocioException, JsonProcessingException;

    @Operation(summary = "Cancelar venda", description = "Cancela uma venda realizada")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "No content"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idVenda}/cancelar")
    ResponseEntity<Void> delete(@PathVariable("idVenda") @Valid @Positive Integer idVenda) throws RegraDeNegocioException, JsonProcessingException;

    @Operation(summary = "Historico de compras do comprador", description = "Lista o historico de compras do comprador " +
    "páginados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de companhias cadastradas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idComprador}/comprador")
    ResponseEntity<PageDTO<VendaDTO>> getByHistoricoCompras(@PathVariable("idComprador") @Valid @Positive Integer id,
                                                            @RequestParam @Valid @Min(0) Integer pagina,
                                                            @RequestParam @Valid @Positive Integer tamanho)
                                                            throws RegraDeNegocioException;

    @Operation(summary = "Historico de vendas da companhia", description = "Lista o historico de vendas da companhia " +
    "páginadas")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de companhias cadastradas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idCompanhia}/companhia")
    ResponseEntity<PageDTO<VendaDTO>> getByHistoricoVendas(@PathVariable("idCompanhia") @Valid @Positive Integer id,
                                                           @RequestParam @Valid @Min(0) Integer pagina,
                                                           @RequestParam @Valid @Positive Integer tamanho)
            throws RegraDeNegocioException;

    @Operation(summary = "Vendas realizadas entre as datas informadas", description = "Lista as vendas dentro do intervalo de tempo informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de vendas encontradas"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/vendas-between")
    public ResponseEntity<PageDTO<VendaDTO>> getVendasBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicioConsulta,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fimConsulta,
                                                              @RequestParam @Valid @Min(0) Integer paginaSolicitada,
                                                              @RequestParam @Valid @Positive Integer tamanhoPagina);
}
