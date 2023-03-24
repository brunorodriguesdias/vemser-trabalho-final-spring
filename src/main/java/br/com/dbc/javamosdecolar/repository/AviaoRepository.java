package br.com.dbc.javamosdecolar.repository;

import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AviaoRepository extends JpaRepository<AviaoEntity, Integer> {
    boolean existsByCodigoAviao(String codigoAviao) ;

    @Query("SELECT new br.com.dbc.javamosdecolar.dto.outs.AviaoDTO(" +
            "c.idUsuario," +
            "a.codigoAviao," +
            "a.capacidade," +
            "a.ultimaManutencao," +
            "a.idAviao," +
            "a.ativo," +
            "c.nomeFantasia" +
            ") FROM AVIAO a" +
            " JOIN COMPANHIA c ON c.idUsuario = a.idCompanhia")
    Page<AviaoDTO> getAllAviaoWithCompanhia(Pageable pageable);

}
