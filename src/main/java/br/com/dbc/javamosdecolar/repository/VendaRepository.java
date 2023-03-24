package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.dto.outs.VendaDTO;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
    @Query("SELECT new br.com.dbc.javamosdecolar.dto.outs.VendaDTO(" +
            "v.idComprador," +
            "v.idPassagem," +
            "v.idVenda," +
            "v.codigo," +
            "v.status," +
            "v.data," +
            "c.nomeFantasia," +
            "v.comprador.nome)" +
            " FROM VENDA v" +
            " JOIN PASSAGEM p ON p.idVenda = v.idVenda" +
            " JOIN VOO vo ON vo.idVoo = p.idVoo" +
            " JOIN AVIAO a ON a.idAviao = vo.idAviao" +
            " JOIN COMPANHIA c ON c.idUsuario = a.idCompanhia" +
            " WHERE v.idComprador = :idComprador")
    Page<VendaDTO> findAllByIdComprador(Integer idComprador,
                                        Pageable pageable);

    @Query("SELECT new br.com.dbc.javamosdecolar.dto.outs.VendaDTO(" +
            "v.idComprador," +
            "v.idPassagem," +
            "v.idVenda," +
            "v.codigo," +
            "v.status," +
            "v.data," +
            "c.nomeFantasia," +
            "v.comprador.nome)" +
            " FROM VENDA v" +
            " JOIN PASSAGEM p ON p.idVenda = v.idVenda" +
            " JOIN VOO vo ON vo.idVoo = p.idVoo" +
            " JOIN AVIAO a ON a.idAviao = vo.idAviao" +
            " JOIN COMPANHIA c ON c.idUsuario = a.idCompanhia" +
            " WHERE v.data BETWEEN :inicio AND :fim")
    Page<VendaDTO> findAllByDataBetween(LocalDateTime inicio,
                                        LocalDateTime fim,
                                        Pageable pageable);

    @Query("SELECT new br.com.dbc.javamosdecolar.dto.outs.VendaDTO(" +
            "v.idComprador," +
            "v.idPassagem," +
            "v.idVenda," +
            "v.codigo," +
            "v.status," +
            "v.data," +
            "c.nomeFantasia," +
            "v.comprador.nome)" +
            " from VENDA v" +
            " JOIN PASSAGEM p ON p.idVenda = v.idVenda" +
            " JOIN VOO vo ON vo.idVoo = p.idVoo" +
            " JOIN AVIAO a ON a.idAviao = vo.idAviao" +
            " JOIN COMPANHIA c ON c.idUsuario = a.idCompanhia" +
            " WHERE c.idUsuario = :idCompanhia")
    Page<VendaDTO> findAllByIdCompanhia(Integer idCompanhia,
                                        Pageable pageable);
}
