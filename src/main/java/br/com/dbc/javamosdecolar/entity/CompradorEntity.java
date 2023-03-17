package br.com.dbc.javamosdecolar.entity;

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
@Table(name = "COMPRADOR")
@PrimaryKeyJoinColumn(name = "ID_USUARIO")
@SQLDelete(sql = "UPDATE AVIACAO.usuario c SET c.ativo = 0 WHERE c.id_usuario=?")
@Where(clause = "ativo = 1")
public class CompradorEntity extends UsuarioEntity {

    @Column(name = "cpf")
    private String cpf;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comprador")
    private Set<VendaEntity> venda;
}
