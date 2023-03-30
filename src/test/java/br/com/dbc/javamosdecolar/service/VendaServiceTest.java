package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.dto.outs.VendaDTO;
import br.com.dbc.javamosdecolar.entity.*;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VendaRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendaServiceTest {
    @InjectMocks
    private VendaService vendaService;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private PassagemService passagemService;
    @Mock
    private CompradorService compradorService;
    @Mock
    private CompanhiaService companhiaService;
    @Mock
    private UsuarioService usuarioService;
    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(vendaService, "objectMapper", objectMapper);
    }
    @Test
    public void deveCriarComSucesso() throws RegraDeNegocioException {
        CompradorEntity compradorEntity = getCompradorEntityMock();
        PassagemEntity passagemEntity = getPassagemEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        VendaEntity vendaEntity = getVendaEntityMock();

        when(compradorService.getLoggedComprador()).thenReturn(compradorEntity);
        when(passagemService.getPassagem(anyInt())).thenReturn(passagemEntity);
        when(passagemService.recuperarCompanhia(anyInt())).thenReturn(companhiaEntity);
        when(vendaRepository.save(any())).thenReturn(vendaEntity);
        when(passagemService.alteraDisponibilidadePassagem(any(), any())).thenReturn(true);

        VendaDTO vendaDTO = vendaService.create(getVendaCreateDTOMock());

        assertNotNull(vendaDTO);
        assertEquals(vendaEntity.getIdVenda(), vendaDTO.getIdVenda());
        assertEquals(vendaEntity.getIdPassagem(), vendaDTO.getIdPassagem());
        assertEquals(vendaEntity.getStatus(), vendaDTO.getStatus());
        assertEquals(vendaEntity.getCodigo(), vendaDTO.getCodigo());
        assertEquals(vendaEntity.getData(), vendaDTO.getData());
        assertEquals(compradorEntity.getNome(), vendaDTO.getComprador());
        assertEquals(companhiaEntity.getNome(), vendaDTO.getCompanhia());
    }
    @Test
    public void deveDeletarComSucesso() throws RegraDeNegocioException {
        VendaEntity vendaEntity = getVendaEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();

        when(vendaRepository.findById(anyInt())).thenReturn(Optional.of(vendaEntity));
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);
        when(companhiaService.recuperarCompanhia(anyString(), anyInt())).thenReturn(companhiaEntity);

        vendaService.delete(vendaEntity.getIdVenda());

        verify(vendaRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPermissaoDeletar() throws RegraDeNegocioException {
        VendaEntity vendaEntity = getVendaEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        companhiaEntity.setIdUsuario(3);
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();
        usuarioDTO.setIdUsuario(getCompanhiaEntityMock().getIdUsuario());

        when(vendaRepository.findById(anyInt())).thenReturn(Optional.of(vendaEntity));
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);
        when(companhiaService.recuperarCompanhia(anyString(), anyInt())).thenReturn(companhiaEntity);

        vendaService.delete(vendaEntity.getIdVenda());
    }

//    @Test
//    public void deveListarComprasCompradorComSucesso() {
//        List<VendaDTO> comprasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
//        Page<VendaDTO> comprasPaginadas = new PageImpl<>(comprasDTO);
//
//    }

    private static VendaCreateDTO getVendaCreateDTOMock() {
        VendaCreateDTO vendaCreateDTO = new VendaCreateDTO();
        vendaCreateDTO.setIdPassagem(1);
        return vendaCreateDTO;
    }

    private static VendaDTO getVendaDTOMock() {
        VendaDTO vendaDTO = new VendaDTO();
        vendaDTO.setIdVenda(1);
        vendaDTO.setCodigo("81318a4b-491b-4b2e-8df4-4241fb8bcf42");
        vendaDTO.setStatus(Status.VENDIDA);
        vendaDTO.setData(LocalDateTime.parse("2023-10-10T16:11:26.2"));
        vendaDTO.setComprador(getCompradorEntityMock().getNome());
        vendaDTO.setIdComprador(1);
        vendaDTO.setCompanhia(getCompanhiaEntityMock().getNome());
        return vendaDTO;
    }

    private static VendaEntity getVendaEntityMock() {
        VendaEntity vendaEntity = new VendaEntity();
        vendaEntity.setIdVenda(1);
        vendaEntity.setCodigo("81318a4b-491b-4b2e-8df4-4241fb8bcf42");
        vendaEntity.setStatus(Status.CONCLUIDO);
        vendaEntity.setData(LocalDateTime.parse("2023-10-10T16:11:26.2"));
        vendaEntity.setIdComprador(2);
        vendaEntity.setIdPassagem(1);
        return vendaEntity;
    }

    private static CompanhiaEntity getCompanhiaEntityMock() {
        CompanhiaEntity companhiaMockadaDoBanco = new CompanhiaEntity();
        companhiaMockadaDoBanco.setIdUsuario(1);
        companhiaMockadaDoBanco.setTipoUsuario(TipoUsuario.COMPANHIA);
        companhiaMockadaDoBanco.setNome("tam aviao");
        companhiaMockadaDoBanco.setNomeFantasia("TAM");
        companhiaMockadaDoBanco.setSenha("1234");
        companhiaMockadaDoBanco.setCnpj("51543712000198");
        companhiaMockadaDoBanco.setAtivo(true);
        return companhiaMockadaDoBanco;
    }

    private static CompradorEntity getCompradorEntityMock() {
        CompradorEntity compradorEntity = new CompradorEntity();
        compradorEntity.setIdUsuario(2);
        compradorEntity.setTipoUsuario(TipoUsuario.COMPRADOR);
        compradorEntity.setNome("Robervaldo");
        compradorEntity.setCpf("68076150000");
        compradorEntity.setAtivo(true);
        return compradorEntity;
    }

    private static PassagemEntity getPassagemEntityMock() {
        PassagemEntity passagemEntity = new PassagemEntity();
        passagemEntity.setIdPassagem(1);
        passagemEntity.setCodigo("71468a4b-491b-4b2e-8df4-4241fb8bcf42");
        passagemEntity.setStatus(Status.DISPONIVEL);
        passagemEntity.setValor(BigDecimal.valueOf(500.50));
        passagemEntity.setNumeroAssento(1);
        passagemEntity.setTipoAssento(TipoAssento.EXECUTIVO);
        passagemEntity.setIdVoo(1);
        return passagemEntity;
    }

    private static UsuarioDTO getUsuarioDTOMock() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(2);
        return usuarioDTO;
    }
}
