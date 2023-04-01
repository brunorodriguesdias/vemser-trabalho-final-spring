package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.AviaoDTO;
import br.com.dbc.javamosdecolar.dto.outs.PassagemDTO;
import br.com.dbc.javamosdecolar.dto.outs.VooDTO;
import br.com.dbc.javamosdecolar.entity.enums.Status;
import br.com.dbc.javamosdecolar.entity.enums.TipoAssento;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;
    @Mock
    private AviaoService aviaoService;
    @Mock
    private PassagemService passagemService;
    @Mock
    private VooService vooService;

    @Test
    public void shouldCreateAviao() throws RegraDeNegocioException {
        //SETUP
        Integer idCompanhia = 1;
        AviaoCreateDTO aviaoCreateDTO = getAviaoCreateDTOMock();
        AviaoDTO aviaoDTO = getAviaoDTOMock();

        when(aviaoService.create(any(AviaoCreateDTO.class))).thenReturn(aviaoDTO);

        //ACT
        AviaoDTO aviaoDTOreturn = adminService.createAviao(idCompanhia, aviaoCreateDTO);

        //ASSERT
        Assert.assertNotNull(aviaoDTOreturn);
        Assert.assertNotNull(aviaoDTO.getIdAviao());
        Assert.assertEquals(aviaoDTO.getNomeCompanhia(),aviaoDTOreturn.getNomeCompanhia());
        Assert.assertEquals(aviaoDTO.getCodigoAviao(), aviaoDTOreturn.getCodigoAviao());
        Assert.assertEquals(idCompanhia, aviaoDTOreturn.getIdCompanhia());
        Assert.assertEquals(aviaoDTO.getUltimaManutencao(), aviaoDTOreturn.getUltimaManutencao());
    }

    @Test
    public void shouldCreatePassagemWithSucess() throws RegraDeNegocioException{
        //SETUP
        Integer idCompanhia = 1;
        PassagemDTO passagemDTO = getPassagemDTOMock();
        PassagemCreateDTO passagemCreateDTO = getPassagemCreateDTOMock();

        when(passagemService.create(any(PassagemCreateDTO.class))).thenReturn(passagemDTO);

        //ACT
        PassagemDTO passagemDTOreturn = adminService.createPassagem(idCompanhia, passagemCreateDTO);

        //ASSERT
        Assert.assertNotNull(passagemDTOreturn);
        Assert.assertEquals(passagemDTO.getIdPassagem(), passagemCreateDTO.getIdVoo());
        Assert.assertEquals(passagemDTO.getValor(), passagemCreateDTO.getValor());
        Assert.assertEquals(passagemDTO.getTipoAssento(), passagemCreateDTO.getTipoAssento());
    }

    @Test
    public void shouldCreateVooWithSucess() throws  RegraDeNegocioException{
        //SETUP
        Integer idCompanhia = 1;
        VooDTO vooDTO = getVooDTOMock();
        VooCreateDTO vooCreateDTO = getVooCreateDTOMock();

        when(vooService.create(any(VooCreateDTO.class))).thenReturn(vooDTO);
        //ACT
        VooDTO vooDTOreturn = adminService.createVoo(idCompanhia, vooCreateDTO);

        //ASSERT
        Assert.assertNotNull(vooDTOreturn);
        Assert.assertNotNull(vooDTO.getIdVoo());
        Assert.assertNotNull(vooDTO.getIdAviao());
        Assert.assertEquals(vooDTO.getIdVoo(), vooDTOreturn.getIdVoo());
        Assert.assertEquals(vooDTO.getOrigem(), vooDTOreturn.getOrigem());
        Assert.assertEquals(vooDTO.getDestino(), vooDTOreturn.getDestino());
        Assert.assertEquals(vooDTO.getAssentosDisponiveis(), vooDTOreturn.getAssentosDisponiveis());
        Assert.assertEquals(vooDTO.getDataChegada(), vooDTOreturn.getDataChegada());
        Assert.assertEquals(vooDTO.getDataPartida(), vooDTOreturn.getDataPartida());
        Assert.assertEquals(vooDTO.getNomeCompanhia(), vooDTOreturn.getNomeCompanhia());
    }

    public static AviaoDTO getAviaoDTOMock(){
        AviaoDTO aviaoDTO = new AviaoDTO();
        aviaoDTO.setIdAviao(1);
        aviaoDTO.setAtivo(Boolean.TRUE);
        aviaoDTO.setCodigoAviao("0123456789");
        aviaoDTO.setIdCompanhia(1);
        aviaoDTO.setCapacidade(10);
        aviaoDTO.setNomeCompanhia("TAM");
        aviaoDTO.setUltimaManutencao(LocalDate.now());
        return aviaoDTO;
    }

    public static AviaoCreateDTO getAviaoCreateDTOMock(){
        AviaoCreateDTO aviaoCreateDTO = new AviaoCreateDTO();
        aviaoCreateDTO.setCodigoAviao("0123456789");
        aviaoCreateDTO.setCapacidade(10);
        aviaoCreateDTO.setUltimaManutencao(LocalDate.now());
        aviaoCreateDTO.setIdCompanhia(1);
        return aviaoCreateDTO;
    }

    public static PassagemDTO getPassagemDTOMock(){
        PassagemDTO passagemDTO = new PassagemDTO();
        passagemDTO.setIdPassagem(1);
        passagemDTO.setStatus(Status.DISPONIVEL);
        passagemDTO.setCodigo("0123456789");
        passagemDTO.setIdVenda(1);
        passagemDTO.setNomeCompanhia("TAM");
        passagemDTO.setNumeroAssento(1);
        passagemDTO.setValor(new BigDecimal(100));
        passagemDTO.setIdVoo(1);
        passagemDTO.setTipoAssento(TipoAssento.ECONOMICO);
        return passagemDTO;
    }

    public static PassagemCreateDTO getPassagemCreateDTOMock(){
        PassagemCreateDTO passagemCreateDTO = new PassagemCreateDTO();
        passagemCreateDTO.setValor(new BigDecimal(100));
        passagemCreateDTO.setIdVoo(1);
        passagemCreateDTO.setTipoAssento(TipoAssento.ECONOMICO);
        return passagemCreateDTO;
    }

    public static VooDTO getVooDTOMock(){
        VooDTO vooDTO = new VooDTO();
        vooDTO.setIdAviao(1);
        vooDTO.setOrigem("SALVADOR");
        vooDTO.setDestino("PORTO ALEGRE");
        vooDTO.setNomeCompanhia("TAM");
        vooDTO.setDataPartida(LocalDateTime.now());
        vooDTO.setDataChegada(vooDTO.getDataPartida().plusDays(1));
        vooDTO.setIdVoo(1);
        vooDTO.setAssentosDisponiveis(10);
        return vooDTO;
    }

    public static VooCreateDTO getVooCreateDTOMock(){
        VooCreateDTO vooCreateDTO = new VooCreateDTO();
        vooCreateDTO.setIdAviao(1);
        vooCreateDTO.setOrigem("SALVADOR");
        vooCreateDTO.setDestino("PORTO ALEGRE");
        vooCreateDTO.setDataPartida(LocalDateTime.now());
        vooCreateDTO.setDataChegada(vooCreateDTO.getDataPartida().plusDays(1));
        vooCreateDTO.setAssentosDisponiveis(10);
        return vooCreateDTO;
    }
}
