package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.*;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CompanhiaRepository;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hibernate.annotations.Where;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanhiaServiceTest {

    @InjectMocks
    private CompanhiaService companhiaService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private CompanhiaRepository companhiaRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private CargoService cargoService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ReflectionTestUtils.setField(companhiaService, "objectMapper", objectMapper);

        ReflectionTestUtils.setField(companhiaService, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void shouldListPaginatedWithSuccess() {
        // SETUP
        List<CompanhiaEntity> listaCompanhias = List.of(getCompanhiaEntity(), getCompanhiaEntity(), getCompanhiaEntity());
        Page<CompanhiaEntity> listaPaginada = new PageImpl<>(listaCompanhias);
        when(companhiaRepository.findAll(Mockito.any(Pageable.class))).thenReturn(listaPaginada);

        // ACT
        PageDTO<CompanhiaDTO> companhiasPaginadas = companhiaService.getAll(0, 10);

        // ASSERT
        assertNotNull(companhiasPaginadas);
        assertEquals(listaPaginada.getTotalElements(), companhiasPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), companhiasPaginadas.getQuantidadePaginas());
        assertEquals(0, companhiasPaginadas.getPagina());
    }

    @Test
    public void shouldGeneratePaginatedCompanhiaRelatorioWithSuccess() {
        // SETUP
        List<CompanhiaRelatorioDTO> listaCompanhias = List.of(getCompanhiaRelatorioDTO(), getCompanhiaRelatorioDTO(), getCompanhiaRelatorioDTO());
        Page<CompanhiaRelatorioDTO> listaPaginada = new PageImpl<>(listaCompanhias);
        when(usuarioService.getIdLoggedUser()).thenReturn(1);
        when(companhiaRepository.existsById(anyInt())).thenReturn(true);
        when(companhiaRepository.gerarRelatorioCompanhia(Mockito.any(Pageable.class), anyInt())).thenReturn(listaPaginada);

        // ACT
        PageDTO<CompanhiaRelatorioDTO> companhiasPaginadas = companhiaService.gerarCompanhiaRelatorio(0, 10);

        // ASSERT
        assertNotNull(companhiasPaginadas);
        assertEquals(listaPaginada.getTotalElements(), companhiasPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), companhiasPaginadas.getQuantidadePaginas());
        assertEquals(0, companhiasPaginadas.getPagina());
    }

    @Test
    public void shouldCreateWithSucess() throws RegraDeNegocioException {
        // SETUP
        CompanhiaCreateDTO minhaNovaCompanhia = getCompanhiaCreateDTO();
        CompanhiaEntity companhiaMockada = getCompanhiaEntity();

        /// repository.save() retorna a Companhia criada
        when(companhiaRepository.save(any())).thenReturn(companhiaMockada);


        // ação (ACT)
        CompanhiaDTO companhiaRetornada = companhiaService.create(minhaNovaCompanhia);

        // verificar se deu certo / afirmativa  (ASSERT)
        Assertions.assertNotNull(companhiaRetornada);
        Assertions.assertEquals(minhaNovaCompanhia.getNome(), companhiaRetornada.getNome());
        Assertions.assertEquals(minhaNovaCompanhia.getLogin(), companhiaRetornada.getLogin());

        Assertions.assertEquals(companhiaMockada.getIdUsuario(), companhiaRetornada.getIdUsuario());
        Assertions.assertEquals(companhiaMockada.getCnpj(), companhiaRetornada.getCnpj());
        Assertions.assertTrue(companhiaRetornada.getAtivo());
    }

    @Test
    public void shouldUpdateCompanhiaWithSuccess() throws RegraDeNegocioException {
        // SETUP
        CompanhiaUpdateDTO minhaCompanhiaEditada = getCompanhiaUpdateDTO();
        CompanhiaEntity companhiaMockada = getCompanhiaEntity();
        companhiaMockada.setNomeFantasia(minhaCompanhiaEditada.getNomeFantasia());

        /// repository.save() retorna a Companhia atualizada
        when(companhiaRepository.save(any())).thenReturn(companhiaMockada);
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));


        // ACT
        CompanhiaDTO companhiaRetornada = companhiaService.update(minhaCompanhiaEditada);

        // ASSERT
        Assertions.assertNotNull(companhiaRetornada);
        Assertions.assertEquals(minhaCompanhiaEditada.getNomeFantasia(), companhiaRetornada.getNomeFantasia());
        Assertions.assertNotEquals(minhaCompanhiaEditada.getSenha(), companhiaRetornada.getSenha());
        Assertions.assertEquals(companhiaMockada.getIdUsuario(), companhiaRetornada.getIdUsuario());
        Assertions.assertEquals(companhiaMockada.getCnpj(), companhiaRetornada.getCnpj());
        Assertions.assertTrue(companhiaRetornada.getAtivo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidateIfSenhaIsSame() throws RegraDeNegocioException{
        // SETUP
        CompanhiaUpdateDTO minhaCompanhiaEditada = getCompanhiaUpdateDTO();
        minhaCompanhiaEditada.setSenha("mypassword");

        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        companhiaService.update(minhaCompanhiaEditada);
    }


    @Test
    public void shouldDeleteWithSuccess() throws RegraDeNegocioException {
        // SETUP
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        companhiaService.delete();

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test
    public void shouldDeleteAsAdminWithSuccess() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        String cnpj = "90.451.383/0001-07";
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        companhiaService.delete(idUsuario, cnpj);

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldDeleteAsAdminWithInvalidCnpj() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        String cnpj = "99.000.000/0000-0-";
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        companhiaService.delete(idUsuario, cnpj);

        // ASSERT
        verify(usuarioService, times(1)).deleteById(anyInt());
    }

    @Test
    public void shouldReturnLoggedCompanhiaWithSuccess() throws RegraDeNegocioException {
        // SETUP
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        CompanhiaDTO companhiaRetornada = companhiaService.getLoggedCompanhia();

        // ASSERT
        Assertions.assertNotNull(companhiaRetornada);
    }

    @Test
    public void shouldReturnCompanhiaByParamAndIdentificadorWithSucess() throws RegraDeNegocioException {
        // SETUP
        when(companhiaRepository.findSingleResultByParamAndValue(anyString(), anyInt())).thenReturn(getCompanhiaEntity());

        // ACT
        CompanhiaEntity companhiaRetornada = companhiaService.recuperarCompanhia("idPassagem",
                1);

        // ASSERT
        Assertions.assertNotNull(companhiaRetornada);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidateIfAlreadyCompanhiaWithCnpj() throws RegraDeNegocioException{
        // SETUP
        when(companhiaRepository.existsCompanhiaEntityByCnpjIsContaining(anyString())).thenReturn(true);

        // ACT
        companhiaService.validCnpj("90.451.383/0001-07");
    }

    @Test
    public void shouldReturnCompanhiaLoggedWithSucess() throws RegraDeNegocioException {
        // SETUP
        when(usuarioService.getIdLoggedUser()).thenReturn(1);
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        CompanhiaEntity companhiaRetornada = companhiaService.getCompanhiaSemId();

        // ASSERT
        Assertions.assertNotNull(companhiaRetornada);
    }

    @Test
    public void shouldReturnCompanhiaEntityByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuarioBuscar = 1;
        when(companhiaRepository.findById(anyInt())).thenReturn(Optional.of(getCompanhiaEntity()));

        // ACT
        CompanhiaEntity companhiaRetornada = companhiaService.getCompanhiaComId(idUsuarioBuscar);

        // ASSERT
        Assertions.assertNotNull(companhiaRetornada);
        Assertions.assertEquals(idUsuarioBuscar, companhiaRetornada.getIdUsuario());
    }


    private static CompanhiaCreateDTO getCompanhiaCreateDTO() {
        CompanhiaCreateDTO minhaNovaCompanhia = new CompanhiaCreateDTO(
                "carlos.cunha@email.com",
                "mypassword",
                "Carlos Cunha",
                "Aviões Voadores",
                "90.451.383/0001-07"
        );
        return minhaNovaCompanhia;
    }

    private static CompanhiaEntity getCompanhiaEntity() {
        CompanhiaEntity companhiaMockada = new CompanhiaEntity();
        companhiaMockada.setIdUsuario(1);
        companhiaMockada.setNome("Carlos Cunha");
        companhiaMockada.setLogin("carlos.cunha@email.com");
        companhiaMockada.setSenha("mypassword");
        companhiaMockada.setCnpj("90.451.383/0001-07");
        companhiaMockada.setAtivo(true);
        return companhiaMockada;
    }

    private static CompanhiaRelatorioDTO getCompanhiaRelatorioDTO() {
        CompanhiaRelatorioDTO companhiaRelatorioDTO = new CompanhiaRelatorioDTO(
                1, "Aviões Voadores", "90.451.383/0001-07",
                1, "XXXX-XXXXXXXXX-XXXXXXX-XXXXXXX", 500,
                LocalDate.of(2023, 3, 24), true,
                1, "SALVADOR", "PORTO ALEGRE",
                LocalDateTime.parse("2023-03-24T15:00:00.000"),
                LocalDateTime.parse("2023-03-24T17:00:00.000"),
                Status.DISPONIVEL, 50, 10
        );

        return  companhiaRelatorioDTO;
    }

    private static CompanhiaUpdateDTO getCompanhiaUpdateDTO() {
        CompanhiaUpdateDTO companhiaUpdateDTO = new CompanhiaUpdateDTO(
                "Novo nome", "newpassword"
        );
        return  companhiaUpdateDTO;
    }

}
