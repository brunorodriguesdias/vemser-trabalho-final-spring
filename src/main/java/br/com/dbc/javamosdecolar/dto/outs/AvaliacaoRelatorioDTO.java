package br.com.dbc.javamosdecolar.dto.outs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRelatorioDTO {

    @Schema(description = "Quantidade de avaliações que o sistema recebeu.")
    private Integer qtdAvaliacoes;
    @Schema(description = "Quantidade de compradores cadastrados no sistema.")
    private Integer qtdUsuarios;
    @Schema(description = "Média das notas avaliadas.")
    private Double mediaAvaliacoes;

}
