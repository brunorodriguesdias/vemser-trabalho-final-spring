package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AviaoDTO extends AviaoCreateDTO {

    @Schema(description = "ID do avião", example = "1")
    private Integer idAviao;

    @Schema(description = "Status do avião", example = "true")
    private boolean ativo;

    @Schema(description = "Nome da companhia", example = "Avionária Corp.")
    private String nomeCompanhia;

    public AviaoDTO(Integer idCompanhia, String codigoAviao, Integer capacidade, LocalDate ultimaManutencao,
                    Integer idAviao, boolean ativo, String nomeCompanhia) {
        super(codigoAviao, capacidade, ultimaManutencao, idCompanhia);
        this.idAviao = idAviao;
        this.ativo = ativo;
        this.nomeCompanhia = nomeCompanhia;
    }
}
