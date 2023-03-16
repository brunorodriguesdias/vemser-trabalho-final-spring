package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "COMPANHIA")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public final class CompanhiaEntity extends UsuarioEntity {

    @Column(name = "CNPJ")
    private String cnpj;

    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;
}

