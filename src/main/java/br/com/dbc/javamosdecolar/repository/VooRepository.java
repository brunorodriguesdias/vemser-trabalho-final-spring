package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.VooEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VooRepository extends JpaRepository<VooEntity, Integer> {

    @Query("SELECT v FROM VOO v" +
            " JOIN AVIAO av ON v.idAviao = av.idAviao" +
            " JOIN COMPANHIA c ON c.idUsuario = av.idCompanhia" +
            " WHERE c.idUsuario = :idCompanhia")
    Page<VooEntity> findVooIdCompanhia(Integer idCompanhia);

    Page<VooEntity> findByIdAviao(Integer idAviao, Pageable pageable);
}
