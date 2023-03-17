package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PassagemRepository extends JpaRepository<PassagemEntity, Integer> {
    List<PassagemEntity> findAllByValorIsLessThanEqual(BigDecimal valor);
    List<PassagemEntity> findAllByCompanhia(CompanhiaEntity companhia);
    List<PassagemEntity> findAllByStatusIs(Status status);
}
