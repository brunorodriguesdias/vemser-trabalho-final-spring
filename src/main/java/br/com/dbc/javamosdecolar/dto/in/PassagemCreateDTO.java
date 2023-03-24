package br.com.dbc.javamosdecolar.dto.in;

import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassagemCreateDTO {

    @Schema(description = "Valor da passagem", example = "800", required = true)
    @NotNull(message = "O campo valor não pode estar nulo!")
    private BigDecimal valor;

    @Schema(description = "Tipo do assento", example = "EXECUTIVO", required = true)
    @NotNull(message = "O campo não pode estar nulo!")
    private TipoAssento tipoAssento;

    @Schema(description = "id do voo", example = "1", required = true)
    @NotNull(message = "Informe o id do voo!")
    private Integer idVoo;
}
