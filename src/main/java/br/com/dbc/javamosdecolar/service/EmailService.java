package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.VendaEntity;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final Configuration fmConfiguration;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String template, String email) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("Javamos Decolar");
            mimeMessageHelper.setText(template, true);

//            emailSender.send(mimeMessageHelper.getMimeMessage()); <TODO> descomentar

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RegraDeNegocioException("Erro ao enviar e-mail.");
        }
    }

    public void sendEmail(VendaEntity venda, String acao, CompradorEntity comprador) throws RegraDeNegocioException {
        this.sendEmail(this.getVendaTemplate(venda, acao), comprador.getLogin());
    }

    public void sendEmail(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        this.sendEmail(this.getNovoUsuarioTemplate(usuarioEntity), usuarioEntity.getLogin());
    }

    public String getVendaTemplate(VendaEntity venda, String acao) throws RegraDeNegocioException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", venda.getComprador().getNome());
        dados.put("codigo", venda.getCodigo());
        dados.put("email", from);

        Template template = null;

        try {
            switch (acao) {
                case "CRIAR":
                    template = fmConfiguration.getTemplate("venda-realizada-template.ftl");
                    break;
                case "DELETAR":
                    template = fmConfiguration.getTemplate("venda-cancelada-template.ftl");
                    break;
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);

        } catch (TemplateException | RuntimeException | IOException e) {
            throw new RegraDeNegocioException("Erro ao enviar e-mail.");
        }
    }
    public String getNovoUsuarioTemplate(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", usuarioEntity.getNome());
        dados.put("email", from);

        try {
            Template template = fmConfiguration.getTemplate("usuario-criado-template.ftl");

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);

        } catch (TemplateException | RuntimeException | IOException e) {
            throw new RegraDeNegocioException("Erro ao enviar e-mail.");
        }
    }
}
