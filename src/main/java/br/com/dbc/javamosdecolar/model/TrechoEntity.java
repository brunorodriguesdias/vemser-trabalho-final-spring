package br.com.dbc.javamosdecolar.model;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TRECHO")
public class TrechoEntity {
    private int idTrecho;
    private String origem;
    private String destino;
    private CompanhiaEntity companhiaEntity;
}
