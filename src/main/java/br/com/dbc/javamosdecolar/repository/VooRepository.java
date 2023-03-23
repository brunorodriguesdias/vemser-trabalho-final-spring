package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.VooEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VooRepository extends JpaRepository<VooEntity, Integer> {
}
