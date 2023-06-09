package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VendaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
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
import lombok.extern.java.Log;
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
    @Mock
    private LogService logService;
    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(vendaService, "objectMapper", objectMapper);
    }
    @Test
    public void deveCriarComSucesso() throws RegraDeNegocioException {
        // SETUP
        CompradorEntity compradorEntity = getCompradorEntityMock();
        PassagemEntity passagemEntity = getPassagemEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        VendaEntity vendaEntity = getVendaEntityMock();

        when(compradorService.getLoggedComprador()).thenReturn(compradorEntity);
        when(passagemService.getPassagem(anyInt())).thenReturn(passagemEntity);
        when(passagemService.recuperarCompanhia(anyInt())).thenReturn(companhiaEntity);
        when(vendaRepository.save(any())).thenReturn(vendaEntity);
        when(passagemService.alteraDisponibilidadePassagem(any(), any())).thenReturn(true);


        // ACT
        VendaDTO vendaDTO = vendaService.create(getVendaCreateDTOMock());

        // ASSERT
        assertNotNull(vendaDTO);
        assertEquals(vendaEntity.getIdVenda(), vendaDTO.getIdVenda());
        assertEquals(vendaEntity.getIdPassagem(), vendaDTO.getIdPassagem());
        assertEquals(vendaEntity.getStatus(), vendaDTO.getStatus());
        assertEquals(vendaEntity.getCodigo(), vendaDTO.getCodigo());
        assertEquals(vendaEntity.getData(), vendaDTO.getData());
        assertEquals(compradorEntity.getNome(), vendaDTO.getComprador());
        assertEquals(companhiaEntity.getNome(), vendaDTO.getCompanhia());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPassagemIndisponivel() throws RegraDeNegocioException {
        //SETUP
        PassagemEntity passagemEntity = getPassagemEntityMock();
        VendaEntity vendaEntity = getVendaEntityMock();
        CompradorEntity compradorEntity = getCompradorEntityMock();
        passagemEntity.setStatus(Status.CANCELADO);

        when(compradorService.getLoggedComprador()).thenReturn(compradorEntity);
        when(passagemService.getPassagem(anyInt())).thenReturn(passagemEntity);

        //ACT
        VendaDTO vendaDTO = vendaService.create(getVendaCreateDTOMock());
    }
    @Test
    public void deveDeletarComSucesso() throws RegraDeNegocioException {
        //SETUP
        VendaEntity vendaEntity = getVendaEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();

        when(vendaRepository.findById(anyInt())).thenReturn(Optional.of(vendaEntity));
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);
        when(companhiaService.recuperarCompanhia(anyString(), anyInt())).thenReturn(companhiaEntity);

        //ACT
        vendaService.delete(vendaEntity.getIdVenda());

        //ASSERT
        verify(vendaRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPermissaoDeletar() throws RegraDeNegocioException {
        //SETUP
        VendaEntity vendaEntity = getVendaEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        companhiaEntity.setIdUsuario(3);
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();
        usuarioDTO.setIdUsuario(getCompanhiaEntityMock().getIdUsuario());

        when(vendaRepository.findById(anyInt())).thenReturn(Optional.of(vendaEntity));
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);
        when(companhiaService.recuperarCompanhia(anyString(), anyInt())).thenReturn(companhiaEntity);

        //ACT
        vendaService.delete(vendaEntity.getIdVenda());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarVendaCancelada() throws RegraDeNegocioException {
        //SETUP
        VendaEntity vendaEntity = getVendaEntityMock();
        vendaEntity.setStatus(Status.CANCELADO);

        when(vendaRepository.findById(anyInt())).thenReturn(Optional.of(vendaEntity));

        //ACT
        vendaService.delete(vendaEntity.getIdVenda());
    }

    @Test
    public void deveListarComprasComprador() throws RegraDeNegocioException {
        //SETUP
        CompradorEntity compradorEntity = getCompradorEntityMock();
        UsuarioEntity usuarioEntity = getUsuarioEntityMock();
        Integer pagina = 0;
        Integer tamanho = 3;

        List<VendaDTO> comprasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> comprasPaginadas = new PageImpl<>(comprasDTO);

        when(compradorService.getCompradorComId(anyInt())).thenReturn(compradorEntity);
        when(usuarioService.getIdLoggedUser()).thenReturn(usuarioEntity.getIdUsuario());
        when(vendaRepository.findAllByIdComprador(anyInt(), any())).thenReturn(comprasPaginadas);

        //ACT
        PageDTO<VendaDTO> comprasPaginadasDTO = vendaService.getHistoricoComprasComprador(compradorEntity.getIdUsuario(), pagina, tamanho);

        //ASSERT
        assertNotNull(comprasPaginadasDTO);
        assertEquals(pagina, comprasPaginadasDTO.getPagina());
        assertEquals(tamanho, comprasPaginadasDTO.getTamanho());
        assertEquals(comprasPaginadas.getTotalElements(), comprasPaginadasDTO.getTotalElementos());
        assertEquals(comprasPaginadas.getTotalPages(), comprasPaginadasDTO.getQuantidadePaginas());
        assertEquals(comprasPaginadas.getContent(), comprasPaginadasDTO.getElementos());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPermissaoNegadaComprador() throws RegraDeNegocioException {
        //SETUP
        CompradorEntity compradorEntity = getCompradorEntityMock();
        UsuarioEntity usuarioEntity = getUsuarioEntityMock();
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();
        Integer pagina = 0;
        Integer tamanho = 3;

        List<VendaDTO> comprasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> comprasPaginadas = new PageImpl<>(comprasDTO);

        when(compradorService.getCompradorComId(anyInt())).thenReturn(compradorEntity);
        when(usuarioService.getIdLoggedUser()).thenReturn(usuarioEntity.getIdUsuario());
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);

        //ACT
        PageDTO<VendaDTO> comprasPaginadasDTO = vendaService.getHistoricoComprasComprador(32, pagina, tamanho);
    }

    @Test
    public void deveListarVendasCompanhia() throws RegraDeNegocioException {
        //SETUP
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        UsuarioEntity usuarioEntity = getUsuarioEntityMock();
        usuarioEntity.setIdUsuario(1);
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();
        Integer pagina = 0;
        Integer tamanho = 3;

        List<VendaDTO> vendasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> vendasPaginadas = new PageImpl<>(vendasDTO);

        when(companhiaService.getCompanhiaComId(anyInt())).thenReturn(companhiaEntity);
        when(usuarioService.getIdLoggedUser()).thenReturn(usuarioEntity.getIdUsuario());
        when(vendaRepository.findAllByIdCompanhia(anyInt(), any())).thenReturn(vendasPaginadas);

        //ACT
        PageDTO<VendaDTO> vendasPaginadasDTO = vendaService.getHistoricoVendasCompanhia(companhiaEntity.getIdUsuario(), pagina, tamanho);

        //ASSERT
        assertNotNull(vendasPaginadasDTO);
        assertEquals(pagina, vendasPaginadasDTO.getPagina());
        assertEquals(tamanho, vendasPaginadasDTO.getTamanho());
        assertEquals(vendasPaginadas.getTotalElements(), vendasPaginadasDTO.getTotalElementos());
        assertEquals(vendasPaginadas.getTotalPages(), vendasPaginadasDTO.getQuantidadePaginas());
        assertEquals(vendasPaginadas.getContent(), vendasPaginadasDTO.getElementos());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPermissaoNegadaCompanhia() throws RegraDeNegocioException {
        //SETUP
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        UsuarioEntity usuarioEntity = getUsuarioEntityMock();
        UsuarioDTO usuarioDTO = getUsuarioDTOMock();
        Integer pagina = 0;
        Integer tamanho = 3;

        List<VendaDTO> vendasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> vendasPaginadas = new PageImpl<>(vendasDTO);

        when(companhiaService.getCompanhiaComId(anyInt())).thenReturn(companhiaEntity);
        when(usuarioService.getIdLoggedUser()).thenReturn(usuarioEntity.getIdUsuario());
        when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);

        //ACT
        PageDTO<VendaDTO> vendasPaginadasDTO = vendaService.getHistoricoVendasCompanhia(32, pagina, tamanho);
    }

    @Test
    public void deveTestarVendasBetween() {
        //SETUP
        List<VendaDTO> vendasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> vendasPaginadas = new PageImpl<>(vendasDTO);
        LocalDateTime dataInicio = LocalDateTime.parse("2021-10-10T16:11:26.2");
        LocalDateTime dataFim = LocalDateTime.parse("2024-10-10T16:11:26.2");
        Integer pagina = 0;
        Integer tamanho = 3;

        when(vendaRepository.findAllByDataBetween(any(), any(), any())).thenReturn(vendasPaginadas);

        //ACT
        PageDTO<VendaDTO> vendasBetween = vendaService.getVendasBetween(dataInicio, dataFim, pagina, tamanho);

        //ASSERT
        assertNotNull(vendasBetween);
        assertEquals(pagina, vendasBetween.getPagina());
        assertEquals(tamanho, vendasBetween.getTamanho());
        assertEquals(vendasPaginadas.getTotalElements(), vendasBetween.getTotalElementos());
        assertEquals(vendasPaginadas.getTotalPages(), vendasBetween.getQuantidadePaginas());
        assertEquals(vendasPaginadas.getContent(), vendasBetween.getElementos());
    }

    @Test
    public void deveTestarListaPaginada() {
        //SETUP
        List<VendaDTO> vendasDTO = List.of(getVendaDTOMock(), getVendaDTOMock(), getVendaDTOMock());
        Page<VendaDTO> vendasPaginadas = new PageImpl<>(vendasDTO);
        Integer pagina = 0;
        Integer tamanho = 3;

        //ACT
        PageDTO<VendaDTO> vendasPaginadasDTO = vendaService.listaPaginada(vendasPaginadas,pagina, tamanho);

        //ASSERT
        assertNotNull(vendasPaginadasDTO);
        assertEquals(pagina, vendasPaginadasDTO.getPagina());
        assertEquals(tamanho, vendasPaginadasDTO.getTamanho());
        assertEquals(vendasPaginadas.getTotalElements(), vendasPaginadasDTO.getTotalElementos());
        assertEquals(vendasPaginadas.getTotalPages(), vendasPaginadasDTO.getQuantidadePaginas());
        assertEquals(vendasPaginadas.getContent(), vendasPaginadasDTO.getElementos());
    }

    //OBJETOS DE USO NOS TESTES

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

    private static UsuarioEntity getUsuarioEntityMock() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(2);
        return usuarioEntity;
    }
}
