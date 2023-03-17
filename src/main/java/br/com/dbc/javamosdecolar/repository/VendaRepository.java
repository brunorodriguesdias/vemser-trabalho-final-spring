package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
    List<VendaEntity> findAllByIdComprador(Integer idComprador);

    List<VendaEntity> findAllByIdCompanhia(Integer idCompanhia);
}
