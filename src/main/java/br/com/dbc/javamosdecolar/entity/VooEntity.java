package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VooEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_voo")
    @SequenceGenerator(name = "seq_voo", sequenceName = "seq_voo", allocationSize = 1)
    @Column(name = "ID_VOO")
    private Integer idVoo;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "DESTINO")
    private String destino;

    @Column(name = "DATA_PARTIDA")
    private LocalDateTime dataPartida;

    @Column(name = "DATA_CHEGADA")
    private LocalDateTime dataChegada;

    @Column(name = "ASSENTOS_DISPONIVEIS")
    private Integer assentosDisponiveis;

    @Column(name = "ID_AVIAO")
    private Integer idAviao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AVIAO", referencedColumnName = "ID_AVIAO", insertable = false, updatable = false)
    private AviaoEntity aviao;

}
