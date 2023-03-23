package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AviaoDTO extends AviaoCreateDTO {
    @Schema(description = "ID do avião", example = "1")
    private Integer idAviao;

    @Schema(description = "Status do avião", example = "DISPONIVEL")
    private Status status;
}
