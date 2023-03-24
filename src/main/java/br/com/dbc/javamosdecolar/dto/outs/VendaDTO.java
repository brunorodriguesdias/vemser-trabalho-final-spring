package br.com.dbc.javamosdecolar.dto.outs;

import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VendaDTO extends VendaCreateDTO {

    private Integer idVenda;
    @Schema(description = "codigo de identificacao da passagem",
            example = "81318a4b-491b-4b2e-8df4-4241fb8bcf42")
    private String codigo;

    @Schema(description = "disponibilidade de compra da passagem", example = "true")
    private Status status;

    @Schema(description = "data de realização da venda", example = "2023-10-10T16:11:26.2")
    private LocalDateTime data;

    @Schema(description = "Nome da Companhia", example = "Avionárias Brasil")
    private String companhia;

    @Schema(description = "Nome do Comprador", example = "Celso Carvalho Camargo")
    private String comprador;

    public VendaDTO(Integer idComprador,Integer idPassagem, Integer idVenda,
                    String codigo, Status status, LocalDateTime data,
                    String companhia, String comprador) {
        super(idComprador, idPassagem);
        this.idVenda = idVenda;
        this.codigo = codigo;
        this.status = status;
        this.data = data;
        this.companhia = companhia;
        this.comprador = comprador;
    }
}
