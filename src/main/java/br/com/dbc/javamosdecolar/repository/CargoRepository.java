package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {

    Optional<CargoEntity> findByNome(String nome);
}
