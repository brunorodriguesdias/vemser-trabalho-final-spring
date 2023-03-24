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

@Repository
public interface PassagemRepository extends JpaRepository<PassagemEntity, Integer> {
    Page<PassagemEntity> findAllByValorIsLessThanEqual(BigDecimal valor, Pageable solicitacaoPaginada);
    Page<PassagemEntity> findAllByVoo(VooEntity vooEntity, Pageable solicitacaoPaginada);
    Page<PassagemEntity> findAllByStatusIs(Status status, Pageable solcitacaoPagina);
    @Query("SELECT COALESCE(MAX(p.numeroAssento), 0) FROM PASSAGEM p WHERE p.idVoo = :idVoo")
    Integer findByProximaPassagem(Integer idVoo);

}
