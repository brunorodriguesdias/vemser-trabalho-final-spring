package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VooDTO extends VooCreateDTO {
    @Schema(example = "2")
    private Integer idVoo;

    @Schema(example = "TAM")
    private String nomeCompanhia;
}
