package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AviaoRepository extends JpaRepository<AviaoEntity, Integer> {
}
