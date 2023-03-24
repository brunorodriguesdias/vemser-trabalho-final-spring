package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PassagemDTO extends PassagemCreateDTO {

    @Schema(description = "id da passagem", example = "1")
    private Integer idPassagem;

    @Schema(description = "id da venda", example = "1")
    private Integer idVenda;

    @Schema(description = "numero do assento", example = "1")
    private Integer numeroAssento;

    @Schema(description = "nome da companhia", example = "TAM")
    private String nomeCompanhia;

    @Schema(description = "codigo de identificacao da passagem", example = "81318a4b-491b-4b2e-8df4-4241fb8bcf42")
    private String codigo;

    @Schema(description = "disponibilidade de compra da passagem", example = "true")
    private Status status;

}
