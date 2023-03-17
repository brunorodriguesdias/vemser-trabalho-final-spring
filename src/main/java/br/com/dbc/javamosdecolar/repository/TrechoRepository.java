package br.com.dbc.javamosdecolar.repository;


import br.com.dbc.javamosdecolar.model.CompanhiaEntity;
import br.com.dbc.javamosdecolar.model.TrechoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrechoRepository extends JpaRepository<TrechoEntity, Integer> {
    Optional<TrechoEntity> findAllByOrigemIsAndDestinoIsAndIdCompanhiaIs(
                                                        String origem,
                                                        String destino,
                                                        Integer idCompanhia);
    List<TrechoEntity> findAllByCompanhia(CompanhiaEntity companhia);

}
