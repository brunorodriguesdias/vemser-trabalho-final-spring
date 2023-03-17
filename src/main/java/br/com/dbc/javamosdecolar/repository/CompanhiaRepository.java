package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.model.CompanhiaEntity;
import br.com.dbc.javamosdecolar.model.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanhiaRepository extends JpaRepository<CompanhiaEntity, Integer> {

    Boolean existsCompanhiaEntityByCnpjIsContaining(String cpf);

    CompanhiaEntity findByLoginAndSenha(String login, String senha);
}
