package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AVIAO")
public class AviaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aviao")
    @SequenceGenerator(name = "seq_aviao", sequenceName = "seq_aviao", allocationSize = 1)
    @Column(name = "ID_AVIAO")
    private Integer idAviao;

    @Column(name = "ID_COMPANHIA")
    private Integer idCompanhia;

    @Column(name = "CODIGO_AVIAO")
    private Integer codigoAviao;

    @Column(name = "ORIGEM_AEROPORTO")
    private String origemAeroporto;

    @Column(name = "DESTINO_AEROPORTO")
    private String destinoAeroporto;

    @Column(name = "ASSENTOS_DISPONIVEIS")
    private Integer assentosDisponiveis;

    @Column(name = "CAPACIDADE")
    private Integer capacidade;

    @Column(name = "WIFI")
    private Boolean wifi;

    @Column(name = "SISTEMA_DE_ENTRETERIMENTO")
    private Boolean sistemaDeEntreterimento;

    @Column(name = "ULTIMA_MANUTENCAO")
    private LocalDate ultimaManutencao;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_AVIAO", insertable = false, updatable = false)
    private CompanhiaEntity companhia;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aviao")
    private Set<PassagemEntity> passagens;
}
