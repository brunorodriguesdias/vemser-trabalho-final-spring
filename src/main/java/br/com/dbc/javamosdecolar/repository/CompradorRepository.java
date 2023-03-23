package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<CompradorEntity, Integer> {

//    @Query(value = "SELECT new br.com.dbc.javamosdecolar.dto.CompradorRelatorioDTO(" +
//            " c.nome," +
//            " c.cpf," +
//            " ce.cnpj," +
//            " ce.nome," +
//            " v.codigo," +
//            " v.data," +
//            " p.codigo," +
//            " p.dataPartida," +
//            " p.dataChegada," +
//            " p.status," +
//            " p.valor," +
//            " t.origem," +
//            " t.destino" +
//            ")" +
//            " FROM COMPRADOR c" +
//            " JOIN VENDA v ON v.idComprador = c.idUsuario" +
//            " JOIN PASSAGEM p ON p.idVenda = v.idVenda" +
//            " JOIN TRECHO t ON t.idTrecho = p.idTrecho" +
//            " JOIN COMPANHIA ce on ce.idUsuario = p.idCompanhia")
//    Page<CompradorRelatorioDTO> compradorRelatorio(Pageable pageable);

    Boolean existsCompradorEntityByCpfIsContaining(String cpf);

    @Query("SELECT c FROM COMPRADOR c WHERE c.login = ?1 AND c.senha = ?2 AND c.ativo = true")
    CompradorEntity findByLoginAndSenha(String login, String senha);
}
