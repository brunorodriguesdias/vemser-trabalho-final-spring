package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvalicaoRepository extends MongoRepository<AvaliacaoEntity, String> {
    List<AvaliacaoEntity> findAll();

    List<AvaliacaoEntity> findAllByNota(Integer nota);

    AvaliacaoEntity findByIdAvaliacao(Integer idAvaliacao);
}
