package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<LogEntity, String> {

    List<LogEntity> findAllByIdUsuario(Integer idUsuario);
}
