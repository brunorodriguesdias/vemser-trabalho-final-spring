package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.model.CompanhiaEntity;
import br.com.dbc.javamosdecolar.model.CompradorEntity;
import br.com.dbc.javamosdecolar.model.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
    List<VendaEntity> getAllByComprador(CompradorEntity compradorEntity);

    List<VendaEntity> getAllByCompanhia(CompanhiaEntity companhiaEntity);
}
