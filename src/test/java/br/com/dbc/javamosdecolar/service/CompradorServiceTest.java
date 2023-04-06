package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CompradorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompradorServiceTest {
    @InjectMocks
    private CompradorService compradorService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private CompradorRepository compradorRepository;
    @Mock
    private ProducerService producerService;
    @Mock
    private CargoService cargoService;
    @Mock
    private LogService logService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ReflectionTestUtils.setField(compradorService, "objectMapper", objectMapper);

        ReflectionTestUtils.setField(compradorService, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void shouldListPaginatedWithSuccess() {
        // SETUP
        List<CompradorEntity> listaCompradors = List.of(getCompradorEntity(), getCompradorEntity(), getCompradorEntity());
        Page<CompradorEntity> listaPaginada = new PageImpl<>(listaCompradors);
        when(compradorRepository.findAll(Mockito.any(Pageable.class))).thenReturn(listaPaginada);

        // ACT
        PageDTO<CompradorDTO> compradorsPaginadas = compradorService.getAll(0, 10);

        // ASSERT
        assertNotNull(compradorsPaginadas);
        assertEquals(listaPaginada.getTotalElements(), compradorsPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), compradorsPaginadas.getQuantidadePaginas());
        assertEquals(0, compradorsPaginadas.getPagina());
    }

    @Test
    public void shouldGeneratePaginatedCompradorRelatorioWithSuccess() {
        // SETUP
        List<CompradorRelatorioDTO> listaCompradors = List.of(getCompradorRelatorioDTO(), getCompradorRelatorioDTO(), getCompradorRelatorioDTO());
        Page<CompradorRelatorioDTO> listaPaginada = new PageImpl<>(listaCompradors);
        when(usuarioService.getIdLoggedUser()).thenReturn(1);
        when(compradorRepository.existsById(anyInt())).thenReturn(true);
        when(compradorRepository.gerarRelatorioCompras(Mockito.any(Pageable.class), anyInt())).thenReturn(listaPaginada);

        // ACT
        PageDTO<CompradorRelatorioDTO> compradorsPaginadas = compradorService.gerarRelatorioCompras(0, 10);

        // ASSERT
        assertNotNull(compradorsPaginadas);
        assertEquals(listaPaginada.getTotalElements(), compradorsPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), compradorsPaginadas.getQuantidadePaginas());
        assertEquals(0, compradorsPaginadas.getPagina());
    }

    @Test
    public void shouldCreateWithSucess() throws RegraDeNegocioException, JsonProcessingException {
        // SETUP
        CompradorCreateDTO meuNovoComprador = getCompradorCreateDTO();
        CompradorEntity compradorMockado = getCompradorEntity();

        /// repository.save() retorna a Comprador criada
        when(compradorRepository.save(any())).thenReturn(compradorMockado);


        // ação (ACT)
        CompradorDTO compradorRetornada = compradorService.create(meuNovoComprador);

        // verificar se deu certo / afirmativa  (ASSERT)
        Assertions.assertNotNull(compradorRetornada);
        Assertions.assertEquals(meuNovoComprador.getNome(), compradorRetornada.getNome());
        Assertions.assertEquals(meuNovoComprador.getLogin(), compradorRetornada.getLogin());

        Assertions.assertEquals(compradorMockado.getIdUsuario(), compradorRetornada.getIdUsuario());
        Assertions.assertEquals(compradorMockado.getCpf(), compradorRetornada.getCpf());
        Assertions.assertTrue(compradorRetornada.getAtivo());
    }

    @Test
    public void shouldUpdateCompradorWithSuccess() throws RegraDeNegocioException {
        // SETUP
        String novaSenha = "newpassword";
        CompradorEntity compradorMockado = getCompradorEntity();

        /// repository.save() retorna a Comprador atualizada
        when(compradorRepository.save(any())).thenReturn(compradorMockado);
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));


        // ACT
        CompradorDTO compradorRetornada = compradorService.update(novaSenha);

        // ASSERT
        Assertions.assertNotNull(compradorRetornada);
        Assertions.assertEquals(compradorMockado.getIdUsuario(), compradorRetornada.getIdUsuario());
        Assertions.assertTrue(compradorRetornada.getAtivo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidateIfSenhaIsSame() throws RegraDeNegocioException{
        // SETUP
        String novaSenha = "mypassword";

        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        compradorService.update(novaSenha);
    }


    @Test
    public void shouldDeleteWithSuccess() throws RegraDeNegocioException {
        // SETUP
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        compradorService.delete();

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test
    public void shouldDeleteAsAdminWithSuccess() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        String cpf = "914.029.190-18";
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        compradorService.delete(idUsuario, cpf);

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldDeleteAsAdminWithFail() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        String cpf = "914.029.190-18";
        CompradorEntity compradorEntity = getCompradorEntity();
        compradorEntity.setAtivo(Boolean.FALSE);
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(compradorEntity));

        // ACT
        compradorService.delete(idUsuario, cpf);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldDeleteAsAdminWithInvalidCnpj() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        String cnpj = "99.000.000/0000-0-";
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        compradorService.delete(idUsuario, cnpj);

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test
    public void shouldReturnLoggedCompradorWithSuccess() throws RegraDeNegocioException {
        // SETUP
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        CompradorDTO compradorRetornado = compradorService.getByComprador();

        // ASSERT
        Assertions.assertNotNull(compradorRetornado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidateIfAlreadyCompradorWithCnpj() throws RegraDeNegocioException{
        // SETUP
        when(compradorRepository.existsCompradorEntityByCpfIsContaining(anyString())).thenReturn(true);

        // ACT
        compradorService.validCpf("914.029.190-18");
    }

    @Test
    public void shouldReturnCompradorLoggedWithSucess() throws RegraDeNegocioException {
        // SETUP
        when(usuarioService.getIdLoggedUser()).thenReturn(1);
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        CompradorEntity compradorRetornada = compradorService.getLoggedComprador();

        // ASSERT
        Assertions.assertNotNull(compradorRetornada);
    }

    @Test
    public void shouldReturnCompradorEntityByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuarioBuscar = 1;
        when(compradorRepository.findById(anyInt())).thenReturn(Optional.of(getCompradorEntity()));

        // ACT
        CompradorEntity compradorRetornada = compradorService.getCompradorComId(idUsuarioBuscar);

        // ASSERT
        Assertions.assertNotNull(compradorRetornada);
        Assertions.assertEquals(idUsuarioBuscar, compradorRetornada.getIdUsuario());
    }


    protected static CompradorEntity getCompradorEntity() {
        CompradorEntity compradorMockado = new CompradorEntity();
        compradorMockado.setIdUsuario(1);
        compradorMockado.setNome("Carlos Cunha");
        compradorMockado.setLogin("carlos.cunha@email.com");
        compradorMockado.setSenha("mypassword");
        compradorMockado.setCpf("914.029.190-18");
        compradorMockado.setAtivo(true);
        return compradorMockado;
    }

    private static CompradorCreateDTO getCompradorCreateDTO() {
        CompradorCreateDTO meuNovoComprador = new CompradorCreateDTO(
                "carlos.cunha@email.com",
                "mypassword",
                "Carlos Cunha",
                "914.029.190-18"
                );
        return meuNovoComprador;
    }

    private static CompradorRelatorioDTO getCompradorRelatorioDTO() {
        CompradorRelatorioDTO compradorRelatorioDTO = new CompradorRelatorioDTO(
                1, "Carlos Cunha", "914.029.190-18",
                true,
                1, "XXXX-XXXXXXXXX-XXXXXXX-XXXXXXX",
                LocalDateTime.parse("2023-03-21T00:00:00.000"),
                Status.CONCLUIDO, 1, Status.VENDIDA, BigDecimal.valueOf(400.0), 5,
                TipoAssento.ECONOMICO, 1, "TAM",
                "90.451.383/0001-07", true,
                1, "SALVADOR", "PORTO ALEGRE",
                LocalDateTime.parse("2023-03-24T15:00:00.000"),
                LocalDateTime.parse("2023-03-24T17:00:00.000"),
                Status.CONCLUIDO
        );

        return  compradorRelatorioDTO;
    }
}
