package br.com.dbc.javamosdecolar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TRECHO")
@SQLDelete(sql = "UPDATE AVIACAO.trecho t SET t.status = 1 WHERE t.id_trecho=?")
public class TrechoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trecho")
    @SequenceGenerator(name = "seq_trecho", sequenceName = "seq_trecho", allocationSize = 1)
    @Column(name = "ID_TRECHO")
    private int idTrecho;

    @Column(name = "ORIGEM")
    private String origem;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "DESTINO")
    private String destino;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trecho")
    private Set<PassagemEntity> passagem;
}
