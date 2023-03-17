package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TRECHO")
public class TrechoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trecho")
    @SequenceGenerator(name = "seq_trecho", sequenceName = "seq_trecho", allocationSize = 1)
    @Column(name = "ID_TRECHO")
    private int idTrecho;

    @Column(name = "ORIGEM")
    private String origem;

    @Column(name = "DESTINO")
    private String destino;
    @Column(name = "id_companhia" )
    private Integer idCompanhia;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    private CompanhiaEntity companhia;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "trecho")
    private Set<PassagemEntity> passagem;
}
