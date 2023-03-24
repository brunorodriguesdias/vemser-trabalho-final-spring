package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Boolean existsByLogin(String login);

    Optional<UsuarioEntity> findByLogin(String login);
}
