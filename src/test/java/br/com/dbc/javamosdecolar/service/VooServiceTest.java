package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.entity.AviaoEntity;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.PassagemEntity;
import br.com.dbc.javamosdecolar.entity.VooEntity;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.VooRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.NoArgsConstructor;
import org.junit.Assert;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@NoArgsConstructor
@RunWith(MockitoJUnitRunner.class)
public class VooServiceTest {

    @Spy
    @InjectMocks
    private VooService vooService;
    @Mock
    private VooRepository vooRepository;
    @Mock
    private CompanhiaService companhiaService;
    @Mock
    private AviaoService aviaoService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private LogService logService;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(vooService, "objectMapper", objectMapper);
    }

    @Test
    public void shouldCreateWithSucess() throws RegraDeNegocioException {
        //SETUP
        VooCreateDTO vooCreateDTO = getVooCreateDTO();
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();
        Mockito.when(aviaoService.getAviao(Mockito.anyInt())).thenReturn(vooEntity.getAviao());
        Mockito.when(vooRepository.save(Mockito.any(VooEntity.class))).thenReturn(vooEntity);
        Mockito.when(companhiaService.recuperarCompanhia(Mockito.anyString(), Mockito.anyInt())).thenReturn(companhiaEntity);


        //ACT
        VooDTO vooDTO = vooService.create(vooCreateDTO);

        //ASSERT
        Assert.assertNotNull(vooDTO);
        Assert.assertEquals(vooDTO.getOrigem(), vooEntity.getOrigem());
        Assert.assertEquals(vooDTO.getDestino(), vooEntity.getDestino());
        Assert.assertEquals(vooDTO.getNomeCompanhia(), companhiaEntity.getNome());
        Assert.assertNotNull(vooDTO.getDataPartida());
        Assert.assertNotNull(vooDTO.getDataChegada());
        Assert.assertNotNull(vooDTO.getAssentosDisponiveis());
        Assert.assertNotNull(vooDTO.getIdVoo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldCreateWithFail() throws RegraDeNegocioException {
        //SETUP
        VooCreateDTO vooCreateDTO = getVooCreateDTO();
        VooEntity vooEntity = getVooEntity();
        vooEntity.getAviao().setAtivo(Boolean.FALSE);
        Mockito.when(aviaoService.getAviao(Mockito.anyInt())).thenReturn(vooEntity.getAviao());

        //ACT
        vooService.create(vooCreateDTO);
    }

    @Test
    public void shouldUpdateWithSucess() throws RegraDeNegocioException{
        //SETUP
        Integer id = 3;
        VooCreateDTO vooCreateDTO = getVooCreateDTO();
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();
        Mockito.when(vooRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(vooEntity));
        Mockito.when(vooRepository.save(Mockito.any())).thenReturn(vooEntity);
        Mockito.when(companhiaService.recuperarCompanhia(Mockito.anyString(), Mockito.anyInt())).thenReturn(companhiaEntity);
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        //ACT
        VooDTO vooDTO = vooService.update(id, vooCreateDTO);

        //ASSERT
        Assert.assertNotNull(vooDTO);
        Assert.assertEquals(vooDTO.getOrigem(), vooEntity.getOrigem());
        Assert.assertEquals(vooDTO.getDestino(), vooEntity.getDestino());
        Assert.assertEquals(vooDTO.getNomeCompanhia(), companhiaEntity.getNome());
        Assert.assertNotNull(vooDTO.getDataPartida());
        Assert.assertNotNull(vooDTO.getDataChegada());
        Assert.assertNotNull(vooDTO.getAssentosDisponiveis());
        Assert.assertNotNull(vooDTO.getIdVoo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldUpdateWithFail() throws RegraDeNegocioException{
        //SETUP
        Integer id = 3;
        VooCreateDTO vooCreateDTO = getVooCreateDTO();
        VooEntity vooEntity = getVooEntity();
        vooEntity.setStatus(Status.CANCELADO);
        Mockito.when(vooRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(vooEntity));

        //ACT
        VooDTO vooDTO = vooService.update(id, vooCreateDTO);
    }

    @Test
    public void shouldDeleteWithSucess() throws RegraDeNegocioException{
        //SETUP
        Integer idVoo = 5;
        VooEntity vooEntity = getVooEntity();
        Mockito.doReturn(vooEntity).when(vooService).getVoo(Mockito.anyInt());
        Mockito.when(companhiaService.recuperarCompanhia(anyString(), anyInt()))
                .thenReturn(CompanhiaServiceTest.getCompanhiaEntity());
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());

        //ACT
        vooService.delete(idVoo);

        //ASSERT
        Mockito.verify(vooRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldDeleteWithFail() throws RegraDeNegocioException{
        //SETUP
        Integer idVoo = 5;
        VooEntity vooEntity = getVooEntity();
        vooEntity.setStatus(Status.CANCELADO);
        Mockito.doReturn(vooEntity).when(vooService).getVoo(Mockito.anyInt());

        //ACT
        vooService.delete(idVoo);
    }

    @Test
    public void shouldGetByIdWithSucess() throws RegraDeNegocioException{
        //SETUP
        Integer idVoo = 5;
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();
        Mockito.when(companhiaService.recuperarCompanhia(Mockito.anyString(), Mockito.anyInt())).thenReturn(companhiaEntity);
        Mockito.doReturn(vooEntity).when(vooService).getVoo(Mockito.anyInt());

        //ACT
        VooDTO vooDTO = vooService.getById(idVoo);

        //ASSERT
        Assert.assertNotNull(vooDTO);
        Assert.assertEquals(vooDTO.getOrigem(), vooEntity.getOrigem());
        Assert.assertEquals(vooDTO.getDestino(), vooEntity.getDestino());
        Assert.assertEquals(vooDTO.getNomeCompanhia(), companhiaEntity.getNome());
        Assert.assertNotNull(vooDTO.getDataPartida());
        Assert.assertNotNull(vooDTO.getDataChegada());
        Assert.assertNotNull(vooDTO.getAssentosDisponiveis());
        Assert.assertNotNull(vooDTO.getIdVoo());
    }

    @Test
    public void shouldGetByVooAviaoSucess() throws RegraDeNegocioException{
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 2;
        Integer idAviao = 3;

        Page<VooEntity> vooEntitiesPage = new PageImpl<>(
                List.of(getVooEntity(), getVooEntity())
        );
        PageDTO<VooDTO> pageDTO = new PageDTO<>(vooEntitiesPage.getTotalElements(),
                vooEntitiesPage.getTotalPages(),
                pagina,
                tamanho,
                vooEntitiesPage.getContent().stream()
                .map(vooEntity -> objectMapper.convertValue(vooEntity, VooDTO.class))
                 .toList()
        );

        Mockito.when(vooRepository.findByIdAviao(Mockito.anyInt(), Mockito.any(Pageable.class))).thenReturn(vooEntitiesPage);
        Mockito.doReturn(pageDTO).when(vooService).listaPaginada(Mockito.any(Page.class), Mockito.anyInt(), Mockito.anyInt());

        //ACT
        PageDTO<VooDTO> vooDTOPageDTO = vooService.getByVooAviao(idAviao, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(vooDTOPageDTO);
        Assert.assertEquals(pagina, vooDTOPageDTO.getPagina());
        Assert.assertEquals(tamanho, vooDTOPageDTO.getTamanho());
        Assert.assertEquals(pageDTO.getTotalElementos(), vooDTOPageDTO.getTotalElementos());
        Assert.assertEquals(pageDTO.getQuantidadePaginas(), vooDTOPageDTO.getQuantidadePaginas());
        Assert.assertEquals(pageDTO.getElementos(), vooDTOPageDTO.getElementos());
    }

    @Test
    public void shouldGetByVooCompanhiaSucess() throws RegraDeNegocioException{
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 2;
        Integer idCompanhia = 3;

        Page<VooEntity> vooEntitiesPage = new PageImpl<>(
                List.of(getVooEntity(), getVooEntity())
        );
        PageDTO<VooDTO> pageDTO = new PageDTO<>(vooEntitiesPage.getTotalElements(),
                vooEntitiesPage.getTotalPages(),
                pagina,
                tamanho,
                vooEntitiesPage.getContent().stream()
                .map(vooEntity -> objectMapper.convertValue(vooEntity, VooDTO.class))
                .toList()
        );

        Mockito.when(vooRepository.findVooIdCompanhia(Mockito.anyInt(), Mockito.any(Pageable.class))).thenReturn(vooEntitiesPage);
        Mockito.doReturn(pageDTO).when(vooService).listaPaginada(Mockito.any(Page.class), Mockito.anyInt(), Mockito.anyInt());

        //ACT
        PageDTO<VooDTO> vooDTOPageDTO = vooService.getByVooCompanhia(idCompanhia, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(vooDTOPageDTO);
        Assert.assertEquals(pagina, vooDTOPageDTO.getPagina());
        Assert.assertEquals(tamanho, vooDTOPageDTO.getTamanho());
        Assert.assertEquals(pageDTO.getTotalElementos(), vooDTOPageDTO.getTotalElementos());
        Assert.assertEquals(pageDTO.getQuantidadePaginas(), vooDTOPageDTO.getQuantidadePaginas());
        Assert.assertEquals(pageDTO.getElementos(), vooDTOPageDTO.getElementos());
    }

    @Test
    public void shouldGetByVooAllSucess() throws RegraDeNegocioException {
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 2;

        Page<VooEntity> vooEntitiesPage = new PageImpl<>(
                List.of(getVooEntity(), getVooEntity())
        );
        PageDTO<VooDTO> pageDTO = new PageDTO<>(vooEntitiesPage.getTotalElements(),
                vooEntitiesPage.getTotalPages(),
                pagina,
                tamanho,
                vooEntitiesPage.getContent().stream()
                .map(vooEntity -> objectMapper.convertValue(vooEntity, VooDTO.class))
                .toList()
        );

        Mockito.when(vooRepository.findAll(Mockito.any(Pageable.class))).thenReturn(vooEntitiesPage);
        Mockito.doReturn(pageDTO).when(vooService).listaPaginada(Mockito.any(Page.class), Mockito.anyInt(), Mockito.anyInt());

        //ACT
        PageDTO<VooDTO> vooDTOPageDTO = vooService.getAllVoo(pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(vooDTOPageDTO);
        Assert.assertEquals(pagina, vooDTOPageDTO.getPagina());
        Assert.assertEquals(tamanho, vooDTOPageDTO.getTamanho());
        Assert.assertEquals(pageDTO.getTotalElementos(), vooDTOPageDTO.getTotalElementos());
        Assert.assertEquals(pageDTO.getElementos(), vooDTOPageDTO.getElementos());
    }

    @Test
    public void shouldListaPaginadaSucess() throws RegraDeNegocioException {
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 2;
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Page<VooEntity> vooEntitiesPage = new PageImpl<>(
                List.of(getVooEntity(), getVooEntity(), getVooEntity())
        );

        Mockito.when(companhiaService.recuperarCompanhia(Mockito.anyString(), Mockito.anyInt())).thenReturn(companhiaEntity);

        //ACT
        PageDTO<VooDTO> vooDTOPageDTO = vooService.listaPaginada(vooEntitiesPage, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(vooDTOPageDTO);
        Assert.assertEquals(pagina, vooDTOPageDTO.getPagina());
        Assert.assertEquals(tamanho, vooDTOPageDTO.getTamanho());
        Assert.assertEquals((Integer) vooEntitiesPage.getTotalPages(), vooDTOPageDTO.getQuantidadePaginas());
        Assert.assertEquals((Long) vooEntitiesPage.getTotalElements(), vooDTOPageDTO.getTotalElementos());
        Assert.assertEquals(vooEntitiesPage.getContent().size(), vooDTOPageDTO.getElementos().size());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldListaPaginadaFail() throws RegraDeNegocioException{
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 1;
        Page<VooEntity> vooEntityPage = new PageImpl<>(new ArrayList<>());

        //ACT
        vooService.listaPaginada(vooEntityPage,pagina,tamanho);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidarDatasFail() throws RegraDeNegocioException{
        //SETUP
        LocalDateTime dataChegada = LocalDateTime.now();
        LocalDateTime dataPartida = dataChegada.plusDays(1);

        //ACT
        vooService.validarDatas(dataPartida, dataChegada);
    }

    @Test
    public void shouldGetVooSucess() throws RegraDeNegocioException{
        //SETUP
        VooEntity vooEntity = getVooEntity();
        Mockito.when(vooRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(vooEntity));

        //ACT
        VooEntity vooReturn = vooService.getVoo(vooEntity.getIdVoo());

        //ASSERT
        Assert.assertEquals(vooEntity.getIdVoo(), vooReturn.getIdVoo());
        Assert.assertEquals(vooEntity.getOrigem(), vooReturn.getOrigem());
        Assert.assertEquals(vooEntity.getDestino(), vooReturn.getDestino());
        Assert.assertEquals(vooEntity.getDataPartida(), vooReturn.getDataPartida());
        Assert.assertEquals(vooEntity.getDataChegada(), vooReturn.getDataChegada());
        Assert.assertEquals(vooEntity.getStatus(), vooReturn.getStatus());
        Assert.assertEquals(vooEntity.getAssentosDisponiveis(), vooReturn.getAssentosDisponiveis());
        Assert.assertEquals(vooEntity.getIdAviao(), vooReturn.getIdAviao());
    }

    @Test
    public void shouldUpdateAssentosDisponiveisSucess(){
        //SETUP
        VooEntity vooEntity = getVooEntity();
        Mockito.when(vooRepository.save(Mockito.any(VooEntity.class))).thenReturn(vooEntity);

        //ACT
        VooEntity vooReturn = vooService.updateAssentosDisponiveis(vooEntity);

        //ASSERT
        Assert.assertEquals(vooEntity.getIdVoo(), vooReturn.getIdVoo());
        Assert.assertEquals(vooEntity.getOrigem(), vooReturn.getOrigem());
        Assert.assertEquals(vooEntity.getDestino(), vooReturn.getDestino());
        Assert.assertEquals(vooEntity.getDataPartida(), vooReturn.getDataPartida());
        Assert.assertEquals(vooEntity.getDataChegada(), vooReturn.getDataChegada());
        Assert.assertEquals(vooEntity.getStatus(), vooReturn.getStatus());
        Assert.assertEquals(vooEntity.getAssentosDisponiveis(), vooReturn.getAssentosDisponiveis());
        Assert.assertEquals(vooEntity.getIdAviao(), vooReturn.getIdAviao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldValidarCompanhiaLogarWithFail() throws RegraDeNegocioException {
        //SETUP
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();
        VooEntity passagemEntity = getVooEntity();

        Mockito.when(usuarioService.getLoggedUser()).thenReturn(usuarioDTO);
        Mockito.doReturn(companhiaEntity).when(vooService).recuperarCompanhia(Mockito.anyInt());

        //ACT
        vooService.validarCompanhiaLogada(passagemEntity);
    }

    private static UsuarioDTO getUsuarioDTO(){
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setTipoUsuario(TipoUsuario.COMPANHIA);
        usuarioDTO.setIdUsuario(1);
        return usuarioDTO;
    }

    private static VooCreateDTO getVooCreateDTO(){
        VooCreateDTO vooCreateDTO = new VooCreateDTO();
        vooCreateDTO.setOrigem("SALVADOR");
        vooCreateDTO.setDestino("PORTO ALEGRE");
        vooCreateDTO.setIdAviao(1);
        vooCreateDTO.setDataPartida(LocalDateTime.now());
        vooCreateDTO.setDataChegada(LocalDateTime.now().plusDays(1L));
        vooCreateDTO.setAssentosDisponiveis(100);
        return vooCreateDTO;
    }

    private static VooEntity getVooEntity(){
        VooEntity vooEntity = new VooEntity();
        vooEntity.setAviao(getAviaoEntity());
        vooEntity.setOrigem("SALVADOR");
        vooEntity.setDestino("PORTO ALEGRE");
        vooEntity.setIdAviao(1);
        vooEntity.setDataPartida(LocalDateTime.now());
        vooEntity.setDataChegada(LocalDateTime.now().plusDays(1L));
        vooEntity.setAssentosDisponiveis(100);
        vooEntity.setIdVoo(1);
        return vooEntity;
    }

    private static AviaoEntity getAviaoEntity(){
        AviaoEntity aviaoEntity = new AviaoEntity();
        aviaoEntity.setAtivo(true);
        aviaoEntity.setIdAviao(100);
        aviaoEntity.setCompanhia(getCompanhiaEntity());

        return aviaoEntity;
    }

    private static CompanhiaEntity getCompanhiaEntity() {
        CompanhiaEntity companhiaMockadaDoBanco = new CompanhiaEntity();
        companhiaMockadaDoBanco.setIdUsuario(100);
        companhiaMockadaDoBanco.setTipoUsuario(TipoUsuario.COMPANHIA);
        companhiaMockadaDoBanco.setNome("tam aviao");
        companhiaMockadaDoBanco.setNomeFantasia("TAM");
        companhiaMockadaDoBanco.setSenha("1234");
        companhiaMockadaDoBanco.setCnpj("51543712000198");
        companhiaMockadaDoBanco.setAtivo(true);
        return companhiaMockadaDoBanco;
    }
}
