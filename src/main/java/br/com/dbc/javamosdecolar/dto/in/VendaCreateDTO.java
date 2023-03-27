package br.com.dbc.javamosdecolar.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaCreateDTO {

    @Schema(description = "id da passagem a ser comprada", example = "2", required = true)
    @NotNull(message = "O campo idPassagem não pode estar nulo!")
    private Integer idPassagem;
}
