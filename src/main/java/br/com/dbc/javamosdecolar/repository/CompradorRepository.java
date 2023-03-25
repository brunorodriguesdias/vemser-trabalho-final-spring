package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompradorRepository extends JpaRepository<CompradorEntity, Integer> {

    @Query(value = "SELECT new br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO(" +
            " c.idUsuario," +
            " c.nome," +
            " c.cpf," +
            " c.ativo," +
            " v.idVenda," +
            " v.codigo," +
            " v.data," +
            " v.status," +
            " p.idPassagem," +
            " p.status," +
            " p.valor," +
            " p.numeroAssento," +
            " p.tipoAssento," +
            " co.idUsuario," +
            " co.nome," +
            " co.cnpj," +
            " co.ativo," +
            " vo.idVoo," +
            " vo.origem," +
            " vo.destino," +
            " vo.dataPartida," +
            " vo.dataChegada," +
            " vo.status" +
            ")" +
            " FROM COMPRADOR c" +
            " JOIN VENDA v ON c.idUsuario = v.idComprador" +
            " JOIN PASSAGEM p ON p.idVenda = v.idVenda" +
            " JOIN VOO vo ON vo.idVoo = p.idVoo" +
            " JOIN AVIAO av ON av.idAviao = vo.idAviao" +
            " JOIN COMPANHIA co ON co.idUsuario = av.idCompanhia" +
            " WHERE :idClausula IS NULL OR c.idUsuario = :idClausula")
    Page<CompradorRelatorioDTO> gerarRelatorioCompras(Pageable pageable, Integer idClausula);

    Boolean existsCompradorEntityByCpfIsContaining(String cpf);

    @Query("SELECT c FROM COMPRADOR c WHERE c.login = ?1 AND c.senha = ?2 AND c.ativo = true")
    CompradorEntity findByLoginAndSenha(String login, String senha);
}
