package br.com.dbc.javamosdecolar.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AVIAO")
public class AviaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aviao")
    @SequenceGenerator(name = "seq_aviao", sequenceName = "seq_aviao", allocationSize = 1)
    private Integer idAviao;
    private Integer codigoAviao;
    private String origem;
    private String destino;
    private Integer assentosDisponiveis;
    private Integer capacidade;
    private Boolean wifi;
    private Boolean sistemaDeEntreterimento;
    private LocalDate ultimaManutencao;
//    private CompanhiaEntity companhiaEntity;

}
