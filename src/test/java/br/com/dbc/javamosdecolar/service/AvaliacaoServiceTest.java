package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AvaliacaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.AvaliacaoRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.AvaliacaoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.AvaliacaoRepository;
import br.com.dbc.javamosdecolar.repository.CompanhiaRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvaliacaoServiceTest {
    @InjectMocks
    private AvaliacaoService avaliacaoService;
    @Mock
    private AvaliacaoRepository avaliacaoRepository;
    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ReflectionTestUtils.setField(avaliacaoService, "objectMapper", objectMapper);
    }

    @Test
    public void shouldListPaginatedWithSuccess() {
        // SETUP
        List<AvaliacaoEntity> listaAvaliacoes = List.of(getAvalicaoEntity(), getAvalicaoEntity(), getAvalicaoEntity());
        Page<AvaliacaoEntity> listaPaginada = new PageImpl<>(listaAvaliacoes);
        when(avaliacaoRepository.findAll(Mockito.any(Pageable.class))).thenReturn(listaPaginada);

        // ACT
        PageDTO<AvaliacaoDTO> companhiasPaginadas = avaliacaoService.findAll(0, 10);

        // ASSERT
        assertNotNull(companhiasPaginadas);
        assertEquals(listaPaginada.getTotalElements(), companhiasPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), companhiasPaginadas.getQuantidadePaginas());
        assertEquals(0, companhiasPaginadas.getPagina());
    }

    @Test
    public void shouldListPaginatedByNotaWithSuccess() {
        // SETUP
        List<AvaliacaoEntity> listaAvaliacoes = List.of(getAvalicaoEntity(), getAvalicaoEntity(), getAvalicaoEntity());
        Page<AvaliacaoEntity> listaPaginada = new PageImpl<>(listaAvaliacoes);
        when(avaliacaoRepository.findAllByNota(anyInt(), Mockito.any(Pageable.class))).thenReturn(listaPaginada);

        // ACT
        PageDTO<AvaliacaoDTO> companhiasPaginadas = avaliacaoService.findAllByNota(0, 10, 5);

        // ASSERT
        assertNotNull(companhiasPaginadas);
        assertEquals(listaPaginada.getTotalElements(), companhiasPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), companhiasPaginadas.getQuantidadePaginas());
        assertEquals(0, companhiasPaginadas.getPagina());

        companhiasPaginadas.getElementos().forEach(avaliacaoDTO ->
                Assertions.assertEquals(5, avaliacaoDTO.getNota()));
    }
    @Test
    public void shouldListPaginatedByNomeWithSuccess() {
        // SETUP
        String palavraBuscada = "Br";
        List<AvaliacaoEntity> listaAvaliacoes = List.of(getAvalicaoEntity(), getAvalicaoEntity(), getAvalicaoEntity());
        Page<AvaliacaoEntity> listaPaginada = new PageImpl<>(listaAvaliacoes);
        when(avaliacaoRepository.findAllByNomeContainingIgnoreCase(anyString(),
                Mockito.any(Pageable.class))).thenReturn(listaPaginada);

        // ACT
        PageDTO<AvaliacaoDTO> companhiasPaginadas = avaliacaoService.findAllByNome(0, 10, palavraBuscada);

        // ASSERT
        assertNotNull(companhiasPaginadas);
        assertEquals(listaPaginada.getTotalElements(), companhiasPaginadas.getTotalElementos());
        assertEquals(listaPaginada.getTotalPages(), companhiasPaginadas.getQuantidadePaginas());
        assertEquals(0, companhiasPaginadas.getPagina());

        companhiasPaginadas.getElementos().forEach(avaliacaoDTO ->
                Assertions.assertTrue(avaliacaoDTO.getNome().contains(palavraBuscada)));
    }

    @Test
    public void shouldGenerateRelatorioWithSuccess() {
        // SETUP
        AvaliacaoRelatorioDTO relatorioEsperado = new AvaliacaoRelatorioDTO(10, null,
                10.0);
        Integer countUsuarios = 10;
        when(avaliacaoRepository.gerarRelatorioAvaliacoes()).thenReturn(relatorioEsperado);
        when(usuarioService.getCountUsers()).thenReturn(countUsuarios);

        // ACT
        AvaliacaoRelatorioDTO relatorioRetornado = avaliacaoService.gerarRelatorio();

        // ASSERT
        Assertions.assertNotNull(relatorioRetornado);
        Assertions.assertEquals(relatorioRetornado.getQtdAvaliacoes(), relatorioEsperado.getQtdAvaliacoes());
        Assertions.assertEquals(relatorioRetornado.getMediaAvaliacoes(), relatorioEsperado.getMediaAvaliacoes());
        Assertions.assertEquals(relatorioRetornado.getQtdUsuarios(), countUsuarios);
    }

    @Test
    public void shouldReturnAvaliacaoByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        String idAvaliacao = "1";
        when(avaliacaoRepository.findById(anyString())).thenReturn(Optional.of(getAvalicaoEntity()));

        // ACT
        AvaliacaoDTO avaliacaoRetornada = avaliacaoService.findByIdAvaliacao(idAvaliacao);

        // ASSERT
        Assertions.assertNotNull(avaliacaoRetornada);
    }

    @Test
    public void shouldCreateWithSucess() throws RegraDeNegocioException {
        // SETUP
        AvaliacaoCreateDTO novaAvalicao = getAvaliacaoCreateDTO();
        AvaliacaoEntity avalicaoMockada = getAvalicaoEntity();

        /// repository.save() retorna a Companhia criada
        when(avaliacaoRepository.save(any())).thenReturn(avalicaoMockada);


        // ação (ACT)
        AvaliacaoDTO avalicaoRetornada = avaliacaoService.create(novaAvalicao);

        // verificar se deu certo / afirmativa  (ASSERT)
        Assertions.assertNotNull(avalicaoRetornada);
        Assertions.assertEquals(novaAvalicao.getNome(), avalicaoRetornada.getNome());
        Assertions.assertEquals(novaAvalicao.getDescricao(), avalicaoRetornada.getDescricao());
        Assertions.assertEquals(novaAvalicao.getNota(), avalicaoRetornada.getNota());
    }

    @Test
    public void shouldDeleteWithSuccess() throws RegraDeNegocioException {
        // SETUP
        String idAvaliacao = "1";
        when(avaliacaoRepository.findById(anyString())).thenReturn(Optional.of(getAvalicaoEntity()));

        // ACT
        avaliacaoService.delete(idAvaliacao);

        // ASSERT
        verify(avaliacaoRepository, times(1)).deleteById(anyString());
    }

    private static AvaliacaoEntity getAvalicaoEntity() {
        return new AvaliacaoEntity("1", "Bruno Rodrigues", 5,
                "Maravilhoso!");
    }

    private static AvaliacaoCreateDTO getAvaliacaoCreateDTO() {
        return new AvaliacaoCreateDTO(
                "Bruno Rodrigues", 5, "Maravilhoso!"
        );
    }

}
