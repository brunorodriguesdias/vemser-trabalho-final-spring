package br.com.dbc.javamosdecolar.docs;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
@Tag(name = "Avaliação")
public interface AvaliacaoDoc {

    @Operation(summary = "Listar avaliações", description = "Lista todas a avaliações")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna lista de avaliações"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAll(@RequestParam @Valid @Min(0) Integer pagina,
                                                         @RequestParam @Valid @Positive Integer tamanho);

    @Operation(summary = "Listar avaliações por nota", description = "Lista avaliações com a nota desejada")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna avaliações com a nota desejada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/listar-nota")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNota(@RequestParam Integer nota,
                                                               @RequestParam @Valid @Min(0) Integer pagina,
                                                               @RequestParam @Valid @Positive Integer tamanho);
    @Operation(summary = "Listar avaliações por nome do avaliador", description = "Lista avaliações cujo nome do " +
            "avaliador contenha a palavra determinada")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna avaliações com a nota desejada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/listar-nome")
    public ResponseEntity<PageDTO<AvaliacaoDTO>> findAllByNome(@RequestParam String nome,
                                                               @RequestParam @Valid @Min(0) Integer pagina,
                                                               @RequestParam @Valid @Positive Integer tamanho);

    @Operation(summary = "Busca avaliação por id", description = "Busca uma avaliação pelo seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a avaliação desejada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/buscar-id")
    public ResponseEntity<AvaliacaoDTO> findByIdAvaliacao(@RequestParam String idAvaliacao) throws RegraDeNegocioException;

    @Operation(summary = "Criar avaliação", description = "Avaliar a plataforma")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a avaliação criada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<AvaliacaoDTO> create (@RequestBody @Valid AvaliacaoCreateDTO avaliacaoCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar avaliação", description = "Deletar uma avaliação pelo seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Retorna que a operação foi realizada"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> delete(@RequestParam String idAvaliacao) throws RegraDeNegocioException;
}
