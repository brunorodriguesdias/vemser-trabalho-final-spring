package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.outs.LogDTO;
import br.com.dbc.javamosdecolar.entity.LogEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<LogDTO> consultLogUsuario(Integer idUsuario){
        List<LogEntity> logEntities = logRepository.findAllByIdUsuario(idUsuario);
        return logEntities.stream()
                .map(logEntity -> objectMapper.convertValue(logEntity, LogDTO.class))
                .toList();
    }
}
