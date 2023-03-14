package br.com.dbc.javamosdecolar.repository;


import br.com.dbc.javamosdecolar.model.CompradorEntity;
import br.com.dbc.javamosdecolar.model.TrechoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrechoRepository extends JpaRepository<TrechoEntity, Integer> {
}
