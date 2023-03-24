package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PassagemRepository extends JpaRepository<PassagemEntity, Integer> {
    List<PassagemEntity> findAllByValorIsLessThanEqual(BigDecimal valor);
    List<PassagemEntity> findAllByVoo(VooEntity vooEntity);
    Page<PassagemEntity> findAllByStatusIs(Status status, Pageable solcitacaoPagina);
    @Query("SELECT COALESCE(MAX(p.numeroAssento), 0) FROM PASSAGEM p WHERE p.idVoo = :idVoo")
    Integer findByProximaPassagem(Integer idVoo);

}
