package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.model.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaRepository extends JpaRepository<VendaEntity, Integer> {
}
