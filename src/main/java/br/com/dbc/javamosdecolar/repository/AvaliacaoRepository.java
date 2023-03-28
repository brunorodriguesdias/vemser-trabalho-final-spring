package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends MongoRepository<AvaliacaoEntity, String> {

    Page<AvaliacaoEntity> findAllByNota(Integer nota, Pageable pageable);
}
