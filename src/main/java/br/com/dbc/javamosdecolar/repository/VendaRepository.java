package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
    List<VendaEntity> findAllByIdComprador(Integer idComprador);

    Page<VendaEntity> findAllByDataBetween(Pageable pageable, LocalDateTime inicio, LocalDateTime fim);

    List<VendaEntity> findAllByIdCompanhia(Integer idCompanhia);
}
