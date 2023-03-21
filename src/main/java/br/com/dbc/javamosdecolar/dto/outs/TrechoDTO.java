package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.TrechoCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrechoDTO extends TrechoCreateDTO {
    @Schema(description = "id do trecho", example = "1")
    private int idTrecho;

    @Schema(description = "Disponibilidade do trecho", allowableValues = {"DISPONIVEL", "CANCELADO"})
    private Status status;
}
