package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
    List<VendaEntity> findAllByIdComprador(Integer idComprador);

    Page<VendaEntity> findAllByDataBetween(Pageable pageable, LocalDate inicio, LocalDate fim);

    List<VendaEntity> findAllByIdCompanhia(Integer idCompanhia);
}
