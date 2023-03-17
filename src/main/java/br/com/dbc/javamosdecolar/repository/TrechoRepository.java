package br.com.dbc.javamosdecolar.repository;


import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.TrechoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrechoRepository extends JpaRepository<TrechoEntity, Integer> {
    Optional<TrechoEntity> findAllByOrigemIsAndDestinoIs(
                                                        String origem,
                                                        String destino);

}
