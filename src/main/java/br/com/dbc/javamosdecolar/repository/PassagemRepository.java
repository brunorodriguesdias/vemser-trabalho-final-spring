package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.model.CompradorEntity;
import br.com.dbc.javamosdecolar.model.PassagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagemRepository extends JpaRepository<PassagemEntity, Integer> {
}
