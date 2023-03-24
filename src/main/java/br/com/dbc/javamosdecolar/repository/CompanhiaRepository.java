package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanhiaRepository extends JpaRepository<CompanhiaEntity, Integer> {

    @Query(value = "SELECT new br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO(" +
            " c.idUsuario," +
            " c.nome," +
            " c.cnpj," +
            " av.idAviao," +
            " av.codigoAviao," +
            " av.capacidade," +
            " av.ultimaManutencao," +
            " av.ativo," +
            " v.idVoo," +
            " v.origem," +
            " v.destino," +
            " v.dataPartida," +
            " v.dataChegada," +
            " v.status," +
            " v.assentosDisponiveis," +
            " (SELECT COUNT(p.idPassagem) FROM COMPANHIA c " +
            "            LEFT JOIN AVIAO av ON av.idCompanhia = c.idUsuario" +
            "            LEFT JOIN VOO v ON v.idAviao = av.idAviao" +
            "            LEFT JOIN PASSAGEM p ON p.idVoo = v.idVoo" +
            "            WHERE p.status = 'DISPONIVEL')" +
            ")" +
            " FROM COMPANHIA c" +
            " LEFT JOIN AVIAO av ON av.idCompanhia = c.idUsuario" +
            " LEFT JOIN VOO v ON v.idAviao = av.idAviao" +
            " LEFT JOIN PASSAGEM p ON p.idVoo = v.idVoo")
    Page<CompanhiaRelatorioDTO> companhiaRelatorio(Pageable pageable);

    Boolean existsCompanhiaEntityByCnpjIsContaining(String cpf);

    @Query("SELECT c FROM COMPANHIA c WHERE c.login = ?1 AND c.senha = ?2 AND c.ativo = true")
    CompanhiaEntity findByLoginAndSenha(String login, String senha);

    @Query("SELECT c FROM COMPANHIA c " +
            " JOIN AVIAO av ON av.idCompanhia = c.idUsuario" +
            " JOIN VOO v ON v.idAviao = av.idAviao" +
            " LEFT JOIN PASSAGEM p ON p.idVoo = v.idVoo" +
            " WHERE " +
            " CASE :param" +
            "   WHEN 'idPassagem' THEN p.idPassagem" +
            "   WHEN 'idVoo' THEN v.idVoo" +
            "   WHEN 'idAviao' THEN av.idAviao" +
            " END" +
            " = :value")
    CompanhiaEntity findSingleResultByParamAndValue(String param, Integer value);
}
