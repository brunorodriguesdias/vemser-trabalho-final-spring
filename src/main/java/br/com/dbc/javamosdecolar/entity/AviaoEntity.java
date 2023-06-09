package br.com.dbc.javamosdecolar.entity;

import br.com.dbc.javamosdecolar.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AVIAO")
@SQLDelete(sql = "UPDATE AVIACAO.AVIAO p SET p.ativo = 0 WHERE p.id_aviao=?")
public class AviaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aviao")
    @SequenceGenerator(name = "seq_aviao", sequenceName = "seq_aviao", allocationSize = 1)
    @Column(name = "ID_AVIAO")
    private Integer idAviao;

    @Column(name = "ID_COMPANHIA")
    private Integer idCompanhia;

    @Column(name = "CODIGO_AVIAO")
    private String codigoAviao;

    @Column(name = "CAPACIDADE")
    private Integer capacidade;

    @Column(name = "ULTIMA_MANUTENCAO")
    private LocalDate ultimaManutencao;

    @Column(name = "ATIVO")
    private boolean ativo;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPANHIA", referencedColumnName = "ID_USUARIO", insertable = false, updatable = false)
    private CompanhiaEntity companhia;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "aviao")
    private Set<VooEntity> voos;
}
