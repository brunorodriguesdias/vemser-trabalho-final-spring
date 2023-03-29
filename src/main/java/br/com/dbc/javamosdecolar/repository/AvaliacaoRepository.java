package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoRelatorioDTO;
import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoRepository extends MongoRepository<AvaliacaoEntity, String> {

    Page<AvaliacaoEntity> findAllByNota(Integer nota, Pageable pageable);
    Page<AvaliacaoEntity> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);
    @Aggregation(pipeline = "{ $group: { _id: null, " +
                                        "mediaAvaliacoes: { $avg: \"$nota\" }, " +
                                        "qtdAvaliacoes: { $count: { } } " +
                            "} }, " +
                            "{ $project: {_id:0, mediaAvaliacoes: 1, qtdAvaliacoes: 1} }")
    AvaliacaoRelatorioDTO gerarRelatorioAvaliacoes();
}
