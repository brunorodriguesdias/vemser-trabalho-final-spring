package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassagemRepository extends JpaRepository<PassagemEntity, Integer> {
}
