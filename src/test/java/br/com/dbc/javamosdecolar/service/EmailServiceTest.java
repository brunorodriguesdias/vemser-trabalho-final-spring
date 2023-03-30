package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import freemarker.template.DefaultObjectWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    private Configuration fmConfiguration = new Configuration();

    @Before
    public void init() throws IOException {
        // SETUP
        String from = "company@email.com";
        ReflectionTestUtils.setField(emailService, "from", from);

        fmConfiguration.setDirectoryForTemplateLoading(new File("src/main/resources/templates/"));
        fmConfiguration.setObjectWrapper(new DefaultObjectWrapper());
        ReflectionTestUtils.setField(emailService, "fmConfiguration", fmConfiguration);
    }

    @Test
    public void shouldSendEmailUsuarioCriadoWithSuccess() throws RegraDeNegocioException {
        // SETUP
        Mockito.when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
        Mockito.doNothing().when(emailSender).send(Mockito.any(MimeMessage.class));

        // ACT
        emailService.sendEmail(UsuarioServiceTest.getUsuarioEntity());

        // ASSERT
        Mockito.verify(emailSender, Mockito.times(1))
                .send(Mockito.any(MimeMessage.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldThrowIfCannotAccesTemplateFile() throws IOException, RegraDeNegocioException {
        // SETUP
        fmConfiguration.setDirectoryForTemplateLoading(new File("src/main/resources/"));

        // ACT
        emailService.sendEmail(UsuarioServiceTest.getUsuarioEntity());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldThrowIfCannotFormMessage() throws RegraDeNegocioException {
        // SETUP
        Mockito.when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));

        UsuarioEntity usuarioEntity = UsuarioServiceTest.getUsuarioEntity();
        usuarioEntity.setLogin("");     // Email inválido pro usuáiro


        // ACT
        emailService.sendEmail(usuarioEntity);
    }

    @Test
    public void shouldSendEmailVendaRealizadaWithSuccess() throws RegraDeNegocioException {
        // SETUP
        String acao = "CRIAR";
        VendaEntity vendaRealizada = getVendaMock();

        Mockito.when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
        Mockito.doNothing().when(emailSender).send(Mockito.any(MimeMessage.class));

        // ACT
        emailService.sendEmail(vendaRealizada, acao, vendaRealizada.getComprador());

        // ASSERT
        Mockito.verify(emailSender, Mockito.times(1))
                .send(Mockito.any(MimeMessage.class));
    }

    @Test
    public void shouldSendEmailVendaCanceladaWithSuccess() throws RegraDeNegocioException {
        // SETUP
        String acao = "DELETAR";
        VendaEntity vendaRealizada = getVendaMock();

        Mockito.when(emailSender.createMimeMessage()).thenReturn(new MimeMessage((Session)null));
        Mockito.doNothing().when(emailSender).send(Mockito.any(MimeMessage.class));

        // ACT
        emailService.sendEmail(vendaRealizada, acao, vendaRealizada.getComprador());

        // ASSERT
        Mockito.verify(emailSender, Mockito.times(1))
                .send(Mockito.any(MimeMessage.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldThrowIfCannotAccesVendaTemplateFile() throws IOException, RegraDeNegocioException {
        // SETUP
        String acao = "CRIAR";
        VendaEntity vendaRealizada = getVendaMock();
        fmConfiguration.setDirectoryForTemplateLoading(new File("src/main/resources/"));

        // ACT
        emailService.sendEmail(vendaRealizada, acao, vendaRealizada.getComprador());
    }

    public static VendaEntity getVendaMock(){
        VendaEntity vendaEntity = new VendaEntity();
        vendaEntity.setComprador(CompradorServiceTest.getCompradorEntity());
        vendaEntity.setCodigo("81318a4b-491b-4b2e-8df4-4241fb8bcf42");
        return vendaEntity;
    }
}
