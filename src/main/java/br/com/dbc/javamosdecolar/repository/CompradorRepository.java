package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.model.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<CompradorEntity, Integer> {
}
