package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.outs.LogDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.LogEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public void saveLog(UsuarioEntity usuarioEntity, Object classe, TipoOperacao tipoOperacao){
        String descricao = classe.toString()
                .substring(classe.toString().lastIndexOf(".") + 1);

        LogEntity logEntity = new LogEntity();
        logEntity.setIdUsuario(usuarioEntity.getIdUsuario());
        logEntity.setLogin(usuarioEntity.getLogin());
        logEntity.setDescricao(descricao);
        logEntity.setTipoOperacao(tipoOperacao);
        logRepository.save(logEntity);
    }

    public PageDTO<LogDTO> consultLogsUsuario(Integer idUsuario, Integer pagina, Integer tamanho){
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<LogEntity> logEntities = logRepository.findLog(idUsuario, pageable);
        return new PageDTO<>( logEntities.getTotalElements(),
                logEntities.getTotalPages(),
                pagina,
                tamanho,
                logEntities.stream().map(
                        logEntity -> objectMapper.convertValue(logEntity, LogDTO.class)
                ).toList()
        );
    }
}
