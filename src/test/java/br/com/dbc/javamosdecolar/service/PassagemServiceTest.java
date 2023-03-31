package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.PassagemCreateAmountDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.entity.*;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.PassagemRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class PassagemServiceTest {

    @Spy
    @InjectMocks
    private PassagemService passagemService;
    @Mock
    private PassagemRepository passagemRepository;
    @Mock
    private CompanhiaService companhiaService;
    @Mock
    private VooService vooService;
    @Mock
    private LogService logService;
    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(passagemService, "objectMapper", objectMapper);
    }

    @Test
    public void shouldCreateWithSucess() throws RegraDeNegocioException{
        //SETUP
        Integer nAssento = 1;
        PassagemCreateDTO passagemCreateDTO = getPassagemCreateDTO();
        PassagemEntity passagemEntity = getPassagemEntity();
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Mockito.when(vooService.getVoo(Mockito.anyInt())).thenReturn(vooEntity);
        Mockito.when(passagemRepository.findByProximaPassagem(Mockito.anyInt())).thenReturn(nAssento);
        Mockito.when(passagemRepository.save(Mockito.any(PassagemEntity.class))).thenReturn(passagemEntity);
        Mockito.when(companhiaService.recuperarCompanhia(Mockito.any(), Mockito.any())).thenReturn(companhiaEntity);
        Mockito.when(vooService.updateAssentosDisponiveis(vooEntity)).thenReturn(vooEntity);

        //ACT
        PassagemDTO passagemDTO = passagemService.create(passagemCreateDTO);

        //ASSERT
        Assert.assertNotNull(passagemDTO);
        Assert.assertNotNull(passagemDTO.getIdPassagem());
        Assert.assertNotNull(passagemDTO.getIdVenda());
        Assert.assertNotNull(passagemDTO.getNumeroAssento());
        Assert.assertNotNull(passagemDTO.getValor());
        Assert.assertNotNull(passagemDTO.getIdVoo());
        Assert.assertEquals(companhiaEntity.getNome(), passagemDTO.getNomeCompanhia());
        Assert.assertEquals(passagemEntity.getCodigo(), passagemDTO.getCodigo());
        Assert.assertEquals(passagemEntity.getStatus(), passagemDTO.getStatus());
        Assert.assertEquals(passagemEntity.getTipoAssento(), passagemDTO.getTipoAssento());
        Assert.assertEquals(companhiaEntity.getNome(), passagemDTO.getNomeCompanhia());
    }

    @Test
    public void shouldCreateAmountSucess() throws RegraDeNegocioException{
        //SETUP
        Integer nPassagem = 1;
        PassagemEntity passagem = getPassagemEntity();
        PassagemCreateAmountDTO passagemCreateAmountDTO = getPassagemCreateAmountDTO();
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Mockito.when(vooService.getVoo(Mockito.anyInt())).thenReturn(vooEntity);
        Mockito.when(passagemRepository.findByProximaPassagem(Mockito.anyInt())).thenReturn(nPassagem);
        Mockito.when(companhiaService.getCompanhiaComId(Mockito.any())).thenReturn(companhiaEntity);
        Mockito.when(passagemRepository.save(Mockito.any(PassagemEntity.class))).thenReturn(passagem);
        Mockito.when(vooService.updateAssentosDisponiveis(Mockito.any(VooEntity.class))).thenReturn(vooEntity);

        //ACT
        List<PassagemDTO> passagemDTOList = passagemService.createAmount(passagemCreateAmountDTO);

        //ASSERT
        Assert.assertEquals((int) passagemCreateAmountDTO.getQuantidadeDePassagens(), passagemDTOList.size());
    }

    @Test
    public void shouldUpdateSucess() throws RegraDeNegocioException{
        //SETUP
        PassagemCreateDTO passagemCreateDTO = getPassagemCreateDTO();
        PassagemEntity passagemEntity = getPassagemEntity();
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Mockito.when(passagemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(passagemEntity));
        Mockito.when(vooService.getVoo(Mockito.anyInt())).thenReturn(vooEntity);
        Mockito.doReturn(companhiaEntity).when(passagemService).recuperarCompanhia(Mockito.any());
        Mockito.when(passagemRepository.save(Mockito.any(PassagemEntity.class))).thenReturn(passagemEntity);
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());
        //ACT
        PassagemDTO passagemDTO = passagemService.update(passagemEntity.getIdPassagem(), passagemCreateDTO);

        //ASSERT
        Assert.assertNotNull(passagemDTO);
        Assert.assertEquals(passagemEntity.getValor(), passagemDTO.getValor());
        Assert.assertEquals(passagemEntity.getTipoAssento(), passagemDTO.getTipoAssento());
        Assert.assertEquals(passagemEntity.getIdVoo(), passagemDTO.getIdVoo());
        Assert.assertEquals(companhiaEntity.getNome(), passagemDTO.getNomeCompanhia());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldUpdateFail() throws RegraDeNegocioException{
        //SETUP
        PassagemCreateDTO passagemCreateDTO = getPassagemCreateDTO();
        PassagemEntity passagemEntity = getPassagemEntity();
        passagemEntity.setStatus(Status.CANCELADO);
        VooEntity vooEntity = getVooEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Mockito.when(passagemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(passagemEntity));
        Mockito.when(vooService.getVoo(Mockito.anyInt())).thenReturn(vooEntity);
        Mockito.doReturn(companhiaEntity).when(passagemService).recuperarCompanhia(Mockito.any());
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());
        //ACT
        passagemService.update(passagemEntity.getIdPassagem(), passagemCreateDTO);
    }

    @Test
    public void shouldDeleteSucess() throws RegraDeNegocioException{
        //SETUP
        Integer idPassagem = 5;
        PassagemEntity passagemEntity = getPassagemEntity();
        Mockito.doReturn(passagemEntity).when(passagemService).getPassagem(Mockito.anyInt());
        Mockito.when(companhiaService.recuperarCompanhia(anyString(), anyInt()))
                .thenReturn(CompanhiaServiceTest.getCompanhiaEntity());
        Mockito.when(usuarioService.getLoggedUser()).thenReturn(
                UsuarioServiceTest.getUsuarioDTO());
        //ACT
        passagemService.delete(idPassagem);

        //ASSERT
        Mockito.verify(passagemRepository, Mockito.times(1))
                .deleteById(Mockito.anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldDeleteFail() throws RegraDeNegocioException{
        //SETUP
        Integer idPassagem = 5;
        PassagemEntity passagemEntity = getPassagemEntity();
        passagemEntity.setStatus(Status.CANCELADO);
        Mockito.doReturn(passagemEntity).when(passagemService).getPassagem(Mockito.anyInt());

        //ACT
        passagemService.delete(idPassagem);
    }

    @Test
    public void shouldGetByIdSucess() throws RegraDeNegocioException{
        //SETUP
        Integer idPassagem = 5;
        PassagemEntity passagemEntity = getPassagemEntity();
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();
        Mockito.when(passagemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(passagemEntity));
        Mockito.doReturn(companhiaEntity)
                .when(passagemService).recuperarCompanhia(Mockito.anyInt());

        //ACT
        PassagemDTO passagemDTO = passagemService.getById(idPassagem);

        //ASSERT
        Assert.assertNotNull(passagemDTO);
        Assert.assertNotNull(passagemDTO.getIdPassagem());
        Assert.assertNotNull(passagemDTO.getIdVenda());
        Assert.assertNotNull(passagemDTO.getNumeroAssento());
        Assert.assertNotNull(passagemDTO.getValor());
        Assert.assertNotNull(passagemDTO.getIdVoo());
        Assert.assertEquals(companhiaEntity.getNome(), passagemDTO.getNomeCompanhia());
        Assert.assertEquals(passagemEntity.getCodigo(), passagemDTO.getCodigo());
        Assert.assertEquals(passagemEntity.getStatus(), passagemDTO.getStatus());
        Assert.assertEquals(passagemEntity.getTipoAssento(), passagemDTO.getTipoAssento());
    }

    @Test
    public void shouldGetByValorMaximoSucess(){
        //SETUP
        BigDecimal valorMaximo = new BigDecimal(1000);
        Integer pagina = 0;
        Integer tamanho = 5;

        Page<PassagemEntity> passagemEntities = new PageImpl<>(
                List.of(getPassagemEntity(), getPassagemEntity())
        );
        PageDTO<PassagemDTO> passagemDTOPageDTO = new PageDTO<>(
                passagemEntities.getTotalElements(),
                passagemEntities.getTotalPages(),
                pagina,
                tamanho,
                passagemEntities.getContent().stream()
                .map(passagemEntity -> objectMapper.convertValue(passagemEntity, PassagemDTO.class))
                .toList()
        );

        Mockito.when(passagemRepository
                .findAllByValorIsLessThanEqual(Mockito.any(BigDecimal.class),
                        Mockito.any(Pageable.class))).thenReturn(passagemEntities);
        Mockito.doReturn(passagemDTOPageDTO).when(passagemService)
                .listaPaginada(Mockito.any(Page.class), Mockito.anyInt(), Mockito.anyInt());
        //ACT
        PageDTO<PassagemDTO> pageDTOreturn = passagemService.getByValorMaximo(valorMaximo, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(pageDTOreturn);
        Assert.assertEquals(pagina, pageDTOreturn.getPagina());
        Assert.assertEquals(tamanho, pageDTOreturn.getTamanho());
        Assert.assertEquals(passagemDTOPageDTO.getTotalElementos(), pageDTOreturn.getTotalElementos());
        Assert.assertEquals(passagemDTOPageDTO.getQuantidadePaginas(), pageDTOreturn.getQuantidadePaginas());
        Assert.assertEquals(passagemDTOPageDTO.getElementos(), pageDTOreturn.getElementos());
    }

    @Test
    public void shouldGetByVooSucess() throws RegraDeNegocioException {
        //SETUP
        Integer idVoo = 2;
        Integer pagina = 0;
        Integer tamanho = 5;

        VooEntity vooEntity = getVooEntity();
        Page<PassagemEntity> passagemEntities = new PageImpl<>(
                List.of(getPassagemEntity(), getPassagemEntity())
        );
        PageDTO<PassagemDTO> passagemDTOPageDTO = new PageDTO<>(
                passagemEntities.getTotalElements(),
                passagemEntities.getTotalPages(),
                pagina,
                tamanho,
                passagemEntities.getContent().stream()
                .map(passagemEntity -> objectMapper.convertValue(passagemEntity, PassagemDTO.class))
                .toList()
        );

        Mockito.when(vooService.getVoo(Mockito.anyInt())).thenReturn(vooEntity);
        Mockito.when(passagemRepository
                .findAllByVoo(Mockito.any(VooEntity.class), Mockito.any(Pageable.class)))
                .thenReturn(passagemEntities);
        Mockito.doReturn(passagemDTOPageDTO).when(passagemService)
                .listaPaginada(Mockito.any(Page.class), Mockito.anyInt(), Mockito.anyInt());

        //ACT
        PageDTO<PassagemDTO> pageDTOreturn = passagemService.getByVoo(idVoo, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(pageDTOreturn);
        Assert.assertEquals(pagina, pageDTOreturn.getPagina());
        Assert.assertEquals(tamanho, pageDTOreturn.getTamanho());
        Assert.assertEquals(passagemDTOPageDTO.getTotalElementos(), pageDTOreturn.getTotalElementos());
        Assert.assertEquals(passagemDTOPageDTO.getQuantidadePaginas(), pageDTOreturn.getQuantidadePaginas());
        Assert.assertEquals(passagemDTOPageDTO.getElementos(), pageDTOreturn.getElementos());
    }

    @Test
    public void shouldGetUltimasPassagensSucess(){
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 5;

        Page<PassagemEntity> passagemEntities = new PageImpl<>(
                List.of(getPassagemEntity(), getPassagemEntity())
        );
        PageDTO<PassagemDTO> passagemDTOPageDTO = new PageDTO<>(
                passagemEntities.getTotalElements(),
                passagemEntities.getTotalPages(),
                pagina,
                tamanho,
                passagemEntities.getContent().stream()
                .map(passagemEntity -> objectMapper.convertValue(passagemEntity, PassagemDTO.class))
                .toList()
        );

        Mockito.doReturn(passagemDTOPageDTO).when(passagemService)
                .listaPaginada(Mockito.any(), Mockito.anyInt(), Mockito.anyInt());

        //ACT
        PageDTO<PassagemDTO> pageDTOreturn = passagemService.getUltimasPassagens(pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(pageDTOreturn);
        Assert.assertEquals(pagina, pageDTOreturn.getPagina());
        Assert.assertEquals(tamanho, pageDTOreturn.getTamanho());
        Assert.assertEquals(passagemDTOPageDTO.getTotalElementos(), pageDTOreturn.getTotalElementos());
        Assert.assertEquals(passagemDTOPageDTO.getQuantidadePaginas(), pageDTOreturn.getQuantidadePaginas());
        Assert.assertEquals(passagemDTOPageDTO.getElementos(), pageDTOreturn.getElementos());
    }

    @Test
    public void shouldListaPaginadaSucess() {
        //SETUP
        Integer pagina = 0;
        Integer tamanho = 5;
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Page<PassagemEntity> passagemEntities = new PageImpl<>(
                List.of(getPassagemEntity(), getPassagemEntity())
        );

        Mockito.doReturn(companhiaEntity).when(passagemService)
                .recuperarCompanhia(Mockito.anyInt());

        //ACT
        PageDTO<PassagemDTO> pageDTOreturn = passagemService.listaPaginada(passagemEntities, pagina, tamanho);

        //ASSERT
        Assert.assertNotNull(pageDTOreturn);
        Assert.assertEquals(pagina, pageDTOreturn.getPagina());
        Assert.assertEquals(tamanho, pageDTOreturn.getTamanho());
        Assert.assertEquals((Long) passagemEntities.getTotalElements(), pageDTOreturn.getTotalElementos());
        Assert.assertEquals((Integer) passagemEntities.getTotalPages(), pageDTOreturn.getQuantidadePaginas());
        Assert.assertEquals(passagemEntities.getContent().size(), pageDTOreturn.getElementos().size());
    }

    @Test
    public void shouldRecuperarCompanhiaSucess(){
        //SETUP
        Integer idPassagem = 1;
        CompanhiaEntity companhiaEntity = getCompanhiaEntity();

        Mockito.when(companhiaService.recuperarCompanhia(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(companhiaEntity);

        //ACT
        CompanhiaEntity companhiaEntityRetorno = passagemService.recuperarCompanhia(idPassagem);

        //ASSERT
        Assert.assertNotNull(companhiaEntityRetorno);
    }

    @Test
    public void shouldGetPassagemSucess() throws RegraDeNegocioException {
        //SETUP
        Integer idPassagem = 1;
        PassagemEntity passagemEntity = getPassagemEntity();

        Mockito.when(passagemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(passagemEntity));

        //ACT
        PassagemEntity passagemEntityReturn = passagemService.getPassagem(idPassagem);

        //ASSERT
        Assert.assertNotNull(passagemEntityReturn);
    }

    @Test
    public void shouldAlerarDisponibilidadePassagem(){
        //SETUP
        VendaEntity vendaEntity = new VendaEntity(){
            {
                setIdVenda(50);
            }
        };
        PassagemEntity passagemEntity = getPassagemEntity();

        Mockito.when(passagemRepository.save(Mockito.any(PassagemEntity.class))).thenReturn(passagemEntity);

        //ACT
        Boolean returnBoolean = passagemService.alteraDisponibilidadePassagem(passagemEntity, vendaEntity);

        //ASSERT
        Assert.assertTrue(returnBoolean);
    }

    private static PassagemCreateDTO getPassagemCreateDTO(){
        PassagemCreateDTO passagemCreateDTO = new PassagemCreateDTO();
        passagemCreateDTO.setValor(new BigDecimal(100));
        passagemCreateDTO.setTipoAssento(TipoAssento.ECONOMICO);
        passagemCreateDTO.setIdVoo(1);
        return passagemCreateDTO;
    }

    private static PassagemEntity getPassagemEntity(){
        PassagemEntity passagemEntity = new PassagemEntity();
        passagemEntity.setIdPassagem(1);
        passagemEntity.setCodigo(UUID.randomUUID().toString());
        passagemEntity.setStatus(Status.DISPONIVEL);
        passagemEntity.setValor(new BigDecimal(100));
        passagemEntity.setNumeroAssento(100);
        passagemEntity.setTipoAssento(TipoAssento.ECONOMICO);
        passagemEntity.setIdVenda(1);
        passagemEntity.setIdVoo(1);
        return passagemEntity;
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

    private static AviaoEntity getAviaoEntity(){
        AviaoEntity aviaoEntity = new AviaoEntity();
        aviaoEntity.setAtivo(true);
        aviaoEntity.setIdAviao(100);
        aviaoEntity.setCompanhia(getCompanhiaEntity());
        return aviaoEntity;
    }

    private static PassagemCreateAmountDTO getPassagemCreateAmountDTO(){
        PassagemCreateAmountDTO passagemCreateAmountDTO = new PassagemCreateAmountDTO();
        passagemCreateAmountDTO.setValor(new BigDecimal(100));
        passagemCreateAmountDTO.setTipoAssento(TipoAssento.ECONOMICO);
        passagemCreateAmountDTO.setIdVoo(1);
        passagemCreateAmountDTO.setQuantidadeDePassagens(10);
        return passagemCreateAmountDTO;
    }
}
