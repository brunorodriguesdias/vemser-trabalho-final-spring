package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.CompradorCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompradorRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.CompradorEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoOperacao;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CompradorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@AllArgsConstructor
public class CompradorService {

    private final CompradorRepository compradorRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final CargoService cargoService;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

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

    public PageDTO<CompradorRelatorioDTO> gerarRelatorioCompras(Integer pagina, Integer tamanho){
        Pageable page = PageRequest.of(pagina, tamanho);
        Integer id = null;

        if(compradorRepository.existsById(usuarioService.getIdLoggedUser())){
            id = usuarioService.getIdLoggedUser();
        }

        Page<CompradorRelatorioDTO> pageRelatorios = compradorRepository.gerarRelatorioCompras(page, id);
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
        CompradorEntity compradorEntity = objectMapper.convertValue(compradorCreateDTO, CompradorEntity.class);
        System.out.println(compradorEntity.getLogin());
        compradorEntity.setTipoUsuario(TipoUsuario.COMPRADOR);
        compradorEntity.setSenha(passwordEncoder.encode(compradorCreateDTO.getSenha()));
        compradorEntity.setAtivo(true);
        compradorEntity.setCargos(new HashSet<>());
        compradorEntity.getCargos().add(cargoService.findByNome("ROLE_COMPRADOR"));

        //salvando no bd o novo comprador
        compradorEntity = compradorRepository.save(compradorEntity);
        cargoService.saveCargo(compradorEntity);
        emailService.sendEmail(compradorEntity);
        logService.saveLog(compradorEntity, CompradorEntity.class, TipoOperacao.CRIAR);
        return objectMapper.convertValue(compradorEntity, CompradorDTO.class);
    }

    public CompradorDTO update(String novaSenha) throws RegraDeNegocioException {
        //Retorna o comprador
        CompradorEntity compradorEntity = getLoggedComprador();

        if(compradorEntity.getSenha().equals(novaSenha.trim())){
            throw new RegraDeNegocioException("Senha idêntica! Informe uma senha diferente.");
        }

        //alterando entidade
        compradorEntity.setSenha(passwordEncoder.encode(novaSenha));

        //salvando no bd
        compradorRepository.save(compradorEntity);
        logService.saveLog(compradorEntity, CompradorEntity.class, TipoOperacao.ALTERAR);
        return objectMapper.convertValue(compradorEntity, CompradorDTO.class);
    }

    public void delete() throws RegraDeNegocioException {
        //recuperando comprador
        CompradorEntity comprador = getLoggedComprador();

        //deletando comprador do bd
        logService.saveLog(comprador, CompradorEntity.class, TipoOperacao.DELETAR);
        usuarioService.deleteById(comprador.getIdUsuario());
    }

    public void delete(Integer id, String cpf) throws RegraDeNegocioException {
        //recuperando comprador
        CompradorEntity comprador = getCompradorComId(id);

        if(Boolean.FALSE.equals(comprador.getAtivo())){
            throw new RegraDeNegocioException("Comrpador já desativado!");
        }

        //deletando comprador do bd
        if(comprador.getCpf().trim().equals(cpf.trim())) {
            logService.saveLog(comprador, CompradorEntity.class, TipoOperacao.DELETAR);
            usuarioService.deleteById(comprador.getIdUsuario());
        } else {
            throw new RegraDeNegocioException("CPF Inválido!");
        }
    }

    public CompradorDTO getByComprador() throws RegraDeNegocioException {
        return objectMapper.convertValue(getLoggedComprador(), CompradorDTO.class);
    }

    protected void validCpf(String cpf) throws RegraDeNegocioException {
        if (Boolean.TRUE.equals(compradorRepository.existsCompradorEntityByCpfIsContaining(cpf))) {
            throw new RegraDeNegocioException("Este CPF já está cadastrado!");
        }
    }

    protected CompradorEntity getCompradorComId(Integer id) throws RegraDeNegocioException {
        return compradorRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));
    }

    protected CompradorEntity getLoggedComprador() throws RegraDeNegocioException {
        return compradorRepository.findById(usuarioService.getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Comprador não encontrado!"));
    }
}