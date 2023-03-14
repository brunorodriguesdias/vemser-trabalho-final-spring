package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TRECHO")
public class TrechoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trecho")
    @SequenceGenerator(name = "seq_trecho", sequenceName = "seq_trecho", allocationSize = 1)
    private int idTrecho;
    @Column(name = "ORIGEM")
    private String origem;
    @Column(name = "DESTINO")
    private String destino;
    private CompanhiaEntity companhiaEntity;
}
