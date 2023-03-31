package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<LogEntity, String> {

    Page<LogEntity> findAllByIdUsuario(Integer id, Pageable pageable);

    default Page<LogEntity> findLog(Integer id, Pageable pageable) {
        if (id == null) {
            return findAll(pageable);
        }
        return findAllByIdUsuario(id, pageable);
    }
}
