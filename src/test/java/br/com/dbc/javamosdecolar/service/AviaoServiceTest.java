package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AviaoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.validator.constraints.pl.NIP;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AviaoServiceTest {
    @Spy
    @InjectMocks
    private AviaoService aviaoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AviaoRepository aviaoRepository;

    @Mock
    private CompanhiaService companhiaService;
    @Mock
    private UsuarioService usuarioService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(aviaoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveListarComSucesso() {
        List<AviaoDTO> listaDTO = List.of(getAviaoDTOEntityMock(), getAviaoDTOEntityMock(), getAviaoDTOEntityMock());
        Page<AviaoDTO> listaPaginada = new PageImpl<>(listaDTO);
        when(aviaoRepository.getAllAviaoWithCompanhia(any(Pageable.class))).thenReturn(listaPaginada);

        PageDTO<AviaoDTO> list = aviaoService.getAll(0, 3);

        assertNotNull(list);
        assertEquals(listaPaginada.getTotalElements(), list.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), list.getQuantidadePaginas());
        assertEquals(0, list.getPagina());
        assertEquals(listaPaginada.getSize(), list.getTamanho());
        assertEquals(listaPaginada.getContent(), list.getElementos());
    }

    @Test
    public void deveCriarComSucesso() throws RegraDeNegocioException {
        AviaoCreateDTO aviaoCreateDTO = getAviaoDTOEntityMock();
//        aviaoCreateDTO.setIdCompanhia(1);
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();

        when(aviaoRepository.existsByCodigoAviao(Mockito.anyString())).thenReturn(false);
        when(companhiaService.getCompanhiaComId(any())).thenReturn(companhiaEntity);
        when(aviaoRepository.save(any())).thenReturn(aviaoEntity);
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        AviaoDTO aviaoRetornado = aviaoService.create(aviaoCreateDTO);

        assertNotNull(aviaoRetornado);
        assertTrue(aviaoRetornado.isAtivo());
        assertNotNull(aviaoRetornado.getIdAviao());
        assertEquals(aviaoCreateDTO.getCodigoAviao(), aviaoRetornado.getCodigoAviao());
        assertEquals(aviaoCreateDTO.getCapacidade(), aviaoRetornado.getCapacidade());
        assertEquals(aviaoCreateDTO.getUltimaManutencao(), aviaoRetornado.getUltimaManutencao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveConsultaCodigoExistente() throws RegraDeNegocioException {
        AviaoCreateDTO aviaoCreateDTO = getAviaoDTOEntityMock();
        when(aviaoRepository.existsByCodigoAviao(anyString())).thenReturn(true);

        AviaoDTO aviaoDTO = aviaoService.create(aviaoCreateDTO);
    }

    @Test
    public void deveEditarComSucesso() throws RegraDeNegocioException {
        AviaoCreateDTO aviaoCreateDTO = getAviaoDTOEntityMock();
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();

        when(aviaoRepository.findById(anyInt())).thenReturn(Optional.of(getAviaoEntityMock()));

        when(aviaoRepository.save(any())).thenReturn(aviaoEntity);
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        AviaoDTO aviaoDTO = aviaoService.update(aviaoEntity.getIdAviao(), aviaoCreateDTO);

        assertNotNull(aviaoDTO);
        assertEquals(aviaoEntity.getIdAviao(), aviaoDTO.getIdAviao());
        assertEquals(aviaoCreateDTO.getCodigoAviao(), aviaoDTO.getCodigoAviao());
        assertEquals(aviaoCreateDTO.getCapacidade(), aviaoDTO.getCapacidade());
        assertEquals(aviaoCreateDTO.getUltimaManutencao(), aviaoDTO.getUltimaManutencao());
        assertTrue(aviaoDTO.isAtivo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveConsultaAviaoAtivoUpdate() throws RegraDeNegocioException {
        AviaoCreateDTO aviaoCreateDTO = getAviaoDTOEntityMock();
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        aviaoEntity.setAtivo(false);
        when(aviaoRepository.findById(anyInt())).thenReturn(Optional.of(aviaoEntity));

        AviaoDTO aviaoDTO = aviaoService.update(aviaoEntity.getIdAviao(), aviaoCreateDTO);
    }

    @Test
    public void deveDeletarComSucesso() throws RegraDeNegocioException {
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        Mockito.doReturn(aviaoEntity).when(aviaoService).getAviao(anyInt());
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());


        aviaoService.delete(aviaoEntity.getIdAviao());

        verify(aviaoRepository, times(1)).delete(any(AviaoEntity.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveConsultaAviaoAtivoDelete() throws RegraDeNegocioException {
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        aviaoEntity.setAtivo(false);
        when(aviaoRepository.findById(anyInt())).thenReturn(Optional.of(aviaoEntity));

        aviaoService.delete(aviaoEntity.getIdAviao());
    }

    @Test
    public void deveRetornarPeloId() throws RegraDeNegocioException {
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        Mockito.doReturn(aviaoEntity).when(aviaoService).getAviao(anyInt());
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        AviaoDTO aviaoDTO = aviaoService.getById(1);

        assertNotNull(aviaoDTO);
        assertEquals(aviaoEntity.getIdAviao(), aviaoDTO.getIdAviao());
        assertEquals(aviaoEntity.getCodigoAviao(), aviaoDTO.getCodigoAviao());
        assertEquals(aviaoEntity.getCapacidade(), aviaoDTO.getCapacidade());
        assertEquals(aviaoEntity.getUltimaManutencao(), aviaoDTO.getUltimaManutencao());
        assertTrue(aviaoDTO.isAtivo());

    }

    @Test
    public void DeveRetornarAviao() throws RegraDeNegocioException {
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        when(aviaoRepository.findById(anyInt())).thenReturn(Optional.of(aviaoEntity));

        AviaoEntity aviaoRetornado = aviaoService.getAviao(1);

        assertNotNull(aviaoRetornado);
    }

    @Test
    public void deveTestarCompanhiaLogada() throws RegraDeNegocioException {
        CompanhiaEntity companhiaEntity = getCompanhiaEntityMock();
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        aviaoService.validarCompanhiaLogada(aviaoEntity);

        verify(aviaoService, times(1)).validarCompanhiaLogada(any(AviaoEntity.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarValidacao() throws RegraDeNegocioException {
        UsuarioDTO usuarioDTO = UsuarioServiceTest.getUsuarioDTO();
        AviaoEntity aviaoEntity = getAviaoEntityMock();
        usuarioDTO.setIdUsuario(5);
        usuarioDTO.setTipoUsuario(TipoUsuario.COMPANHIA);
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                usuarioDTO);

        aviaoService.validarCompanhiaLogada(aviaoEntity);
    }


    @NotNull
    private static AviaoDTO getAviaoDTOEntityMock() {
        AviaoDTO aviaoDTOMockadaDoBanco = new AviaoDTO();
        aviaoDTOMockadaDoBanco.setIdAviao(1);
        aviaoDTOMockadaDoBanco.setIdCompanhia(1);
        aviaoDTOMockadaDoBanco.setCodigoAviao("PT-ZXE");
        aviaoDTOMockadaDoBanco.setCapacidade(200);
        aviaoDTOMockadaDoBanco.setUltimaManutencao(LocalDate.of(2000, 9, 8));
        aviaoDTOMockadaDoBanco.setAtivo(true);
        return aviaoDTOMockadaDoBanco;
    }

    @NotNull
    private static AviaoEntity getAviaoEntityMock() {
        AviaoEntity aviaoMockadaDoBanco = new AviaoEntity();
        aviaoMockadaDoBanco.setIdAviao(1);
        aviaoMockadaDoBanco.setIdCompanhia(1);
        aviaoMockadaDoBanco.setCodigoAviao("PT-ZXE");
        aviaoMockadaDoBanco.setCapacidade(200);
        aviaoMockadaDoBanco.setUltimaManutencao(LocalDate.of(2000, 9, 8));
        aviaoMockadaDoBanco.setAtivo(true);
        aviaoMockadaDoBanco.setCompanhia(getCompanhiaEntityMock());
        return aviaoMockadaDoBanco;
    }

    @NotNull
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
}
