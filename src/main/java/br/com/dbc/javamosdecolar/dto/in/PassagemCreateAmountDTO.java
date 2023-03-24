package br.com.dbc.javamosdecolar.dto.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PassagemCreateAmountDTO extends PassagemCreateDTO{

    @Schema(description = "quantidade de passagens", example = "10", required = true)
    @NotNull
    private Integer quantidadeDePassagens;

}
