package br.com.dbc.javamosdecolar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "COMPANHIA")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
public class CompanhiaEntity extends UsuarioEntity {

    @Column(name = "CNPJ")
    private String cnpj;

    @Column(name = "NOME_FANTASIA")
    private String nomeFantasia;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "companhia")
    private Set<VendaEntity> venda;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "companhia")
    private Set<TrechoEntity> trechos;
}

