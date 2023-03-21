package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CompradorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CompradorService {

    private final CompradorRepository compradorRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public PageDTO<CompradorDTO> getAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<CompradorEntity> compradoresPaginados = compradorRepository.findAll(solcitacaoPagina);

        List<CompradorDTO> compradores = compradoresPaginados.getContent().stream()
                .map(pessoa -> objectMapper.convertValue(pessoa, CompradorDTO.class))
                .toList();

        return new PageDTO<>(compradoresPaginados.getTotalElements(),
                compradoresPaginados.getTotalPages(),
                pagina,
                tamanho,
                compradores);
    }

    public PageDTO<CompradorRelatorioDTO> compradorRelatorio(Integer pagina, Integer tamanho){
        Pageable page = PageRequest.of(pagina, tamanho);
        Page<CompradorRelatorioDTO> pageRelatorios = compradorRepository.compradorRelatorio(page);

        List<CompradorRelatorioDTO> relatorios = pageRelatorios.getContent().stream().toList();

        return new PageDTO<>(pageRelatorios.getTotalElements(),
                pageRelatorios.getTotalPages(),
                pagina,
                tamanho,
                relatorios);
    }

    public CompradorDTO create(CompradorCreateDTO compradorCreateDTO) throws RegraDeNegocioException {
        //validando se o login se já está registrado
        usuarioService.existsLogin(compradorCreateDTO.getLogin());
        //validando se cpf já está registrado
        validCpf(compradorCreateDTO.getCpf());

        //editando e adicionando usuario ao comprador
        CompradorEntity comprador = objectMapper.convertValue(compradorCreateDTO, CompradorEntity.class);
        comprador.setTipoUsuario(TipoUsuario.COMPRADOR);
        comprador.setSenha(compradorCreateDTO.getSenha());
        comprador.setAtivo(true);

        //salvando no bd o novo comprador
        compradorRepository.save(comprador);
        emailService.sendEmail(comprador);
        return objectMapper.convertValue(comprador, CompradorDTO.class);
    }

    public CompradorDTO update(String login, String senha, String novaSenha) throws RegraDeNegocioException {
        //Retorna o comprador
        CompradorEntity compradorEntity = getLoginSenha(login,senha);

        if(compradorEntity.getSenha().equals(novaSenha.trim())){
            throw new RegraDeNegocioException("Senha idêntica! Informe uma senha diferente.");
        }

        //alterando entidade
        compradorEntity.setSenha(novaSenha);

        //salvando no bd
        compradorRepository.save(compradorEntity);

        return objectMapper.convertValue(compradorEntity, CompradorDTO.class);

    }

    public void delete(String login, String senha, String cpf) throws RegraDeNegocioException {
        //recuperando comprador
        CompradorEntity comprador = getLoginSenha(login,senha);

        //deletando comprador do bd
        if(comprador.getCpf().trim().equals(cpf.trim())) {
            usuarioService.deleteById(comprador.getIdUsuario());
        } else {
            throw new RegraDeNegocioException("CPF Inválido!");
        }

    }

    public CompradorDTO getLoginSenhaReturn(String login, String senha) throws RegraDeNegocioException {
        return objectMapper.convertValue(getLoginSenha(login,senha), CompradorDTO.class);
    }

    protected void validCpf(String cpf) throws RegraDeNegocioException {
        if (Boolean.TRUE.equals(compradorRepository.existsCompradorEntityByCpfIsContaining(cpf))) {
            throw new RegraDeNegocioException("Este CPF já está cadastrado!");
        }
    }

    protected CompradorEntity getComprador(Integer id) throws RegraDeNegocioException {
        return compradorRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrada"));
    }

    protected CompradorEntity getLoginSenha(String login, String senha) throws RegraDeNegocioException {
        CompradorEntity comprador = compradorRepository.findByLoginAndSenha(login,senha);

        if(comprador == null){
            throw new RegraDeNegocioException("Login e Senha inválidos!");
        }
        return comprador;
    }
}