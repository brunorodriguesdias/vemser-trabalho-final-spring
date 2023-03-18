package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trecho")
    private Set<PassagemEntity> passagem;
}
