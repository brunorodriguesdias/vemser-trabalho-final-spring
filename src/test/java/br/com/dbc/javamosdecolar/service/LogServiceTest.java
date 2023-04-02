package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.outs.LogDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.LogEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.repository.LogRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {

    @InjectMocks
    private LogService logService;
    @Mock
    private LogRepository logRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(logService, "objectMapper", objectMapper);
    }

    @Test
    public void shouldSaveLogSucess(){
        //SETUP
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Class<UsuarioEntity> usuarioEntityClass = UsuarioEntity.class;
        TipoOperacao tipoOperacao = TipoOperacao.CRIAR;

        //ACT
        logService.saveLog(usuarioEntity, usuarioEntityClass, tipoOperacao);

        //ASSERT
        Mockito.verify(logRepository, Mockito.times(1)).save(Mockito.any());
    }


    @Test
    public void shouldConsultLogsUsuario(){
        //SETUP
        Page<LogEntity> logEntities = new PageImpl<>(List.of(getLogEntity()));
        Integer pagina = 0;
        Integer tamanho = 1;
        Integer idUsuario = 1;

        Mockito.when(logRepository
                .findLog(Mockito.anyInt(),
                        Mockito.any(Pageable.class)))
                .thenReturn(logEntities);
        //ACT
        PageDTO<LogDTO> pageDTO = logService.consultLogsUsuario(idUsuario, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(pageDTO);
        Assert.assertEquals((long) pageDTO.getTotalElementos(), logEntities.getTotalElements());
        Assert.assertEquals((int) pageDTO.getQuantidadePaginas(), logEntities.getTotalPages());
        Assert.assertEquals(pageDTO.getPagina(), pagina);
        Assert.assertEquals(pageDTO.getTamanho(), tamanho);
        Assert.assertEquals(pageDTO.getElementos().size(), logEntities.getContent().size());
    }

    public static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setTipoUsuario(TipoUsuario.ADMIN);
        usuarioEntity.setNome("João");
        usuarioEntity.setAtivo(Boolean.TRUE);
        usuarioEntity.setSenha("123456");
        usuarioEntity.setLogin("email@email.com");
        return usuarioEntity;
    }

    public static LogEntity getLogEntity(){
        LogEntity logEntity = new LogEntity();
        logEntity.setIdLog(new ObjectId());
        logEntity.setDescricao("UsuarioEntity");
        logEntity.setTipoOperacao(TipoOperacao.CRIAR);
        logEntity.setLogin("email@email.com");
        logEntity.setIdUsuario(1);
        return logEntity;
    }
}
