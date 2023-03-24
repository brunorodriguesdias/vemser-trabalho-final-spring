package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanhiaRepository extends JpaRepository<CompanhiaEntity, Integer> {

//    @Query(value = "SELECT new br.com.dbc.javamosdecolar.dto.CompanhiaRelatorioDTO(" +
//            " c.nome," +
//            " c.cnpj," +
//            " p.codigo," +
//            " p.dataPartida," +
//            " p.dataChegada," +
//            " p.status," +
//            " p.valor," +
//            " t.origem," +
//            " t.destino," +
//            " v.codigo," +
//            " v.data," +
//            " v.status," +
//            " c2.nome," +
//            " c2.cpf" +
//            ") " +
//            " FROM PASSAGEM p" +
//            " JOIN COMPANHIA c ON c.idUsuario = p.idCompanhia" +
//            " LEFT JOIN VENDA v ON v.idVenda = p.idVenda" +
//            " JOIN TRECHO t ON t.idTrecho = p.idTrecho" +
//            " LEFT JOIN COMPRADOR c2 ON c2.idUsuario = v.idComprador" +
//            " ORDER BY v.data ASC")
//    Page<CompanhiaRelatorioDTO> companhiaRelatorio(Pageable pageable);

    Boolean existsCompanhiaEntityByCnpjIsContaining(String cpf);

    @Query("SELECT c FROM COMPANHIA c WHERE c.login = ?1 AND c.senha = ?2 AND c.ativo = true")
    CompanhiaEntity findByLoginAndSenha(String login, String senha);

    @Query("SELECT c FROM COMPANHIA c " +
            " JOIN AVIAO av ON av.idCompanhia = c.idUsuario" +
            " JOIN VOO v ON v.idAviao = av.idAviao" +
            " JOIN PASSAGEM p ON p.idVoo = v.idVoo" +
            " WHERE " +
            " CASE :param" +
            "   WHEN 'idPassagem' THEN p.idPassagem" +
            "   WHEN 'idVoo' THEN v.idVoo" +
            "   WHEN 'idAviao' THEN av.idAviao" +
            " END" +
            " = :value")
    CompanhiaEntity findCompanhia(String param, Integer value);
}
