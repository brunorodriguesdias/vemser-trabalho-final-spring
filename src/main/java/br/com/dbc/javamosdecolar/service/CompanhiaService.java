package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.CompanhiaCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.CompanhiaUpdateDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaDTO;
import br.com.dbc.javamosdecolar.dto.outs.CompanhiaRelatorioDTO;
import br.com.dbc.javamosdecolar.dto.outs.PageDTO;
import br.com.dbc.javamosdecolar.entity.CompanhiaEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.CompanhiaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanhiaService {

    private final CompanhiaRepository companhiaRepository;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
//    private final EmailService emailService;
    private final CargoService cargoService;
    private final PasswordEncoder passwordEncoder;

    public PageDTO<CompanhiaDTO> getAll(Integer pagina, Integer tamanho) {
        Pageable solcitacaoPagina = PageRequest.of(pagina, tamanho);
        Page<CompanhiaEntity> listaPaginada = companhiaRepository.findAll(solcitacaoPagina);

        List<CompanhiaDTO> listaCompanhiaDTO = listaPaginada
                .map(companhiaEntity -> objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class))
                .toList();

        return new PageDTO<>(listaPaginada.getTotalElements(),
        listaPaginada.getTotalPages(),
        pagina,
        tamanho,
        listaCompanhiaDTO);
    }

    public PageDTO<CompanhiaRelatorioDTO> companhiaRelatorio(Integer pagina, Integer tamanho){
        Pageable page = PageRequest.of(pagina, tamanho);
        Integer id = null;

        if(companhiaRepository.existsById(usuarioService.getIdLoggedUser())){
            id = usuarioService.getIdLoggedUser();
        }

        Page<CompanhiaRelatorioDTO> pageRelatorios = companhiaRepository.gerarRelatorioCompanhia(page, id);
        List<CompanhiaRelatorioDTO> relatorios = pageRelatorios.getContent().stream().toList();

        return new PageDTO<>(pageRelatorios.getTotalElements(),
                pageRelatorios.getTotalPages(),
                pagina,
                tamanho,
                relatorios);
    }

    public CompanhiaDTO create(CompanhiaCreateDTO companhiaCreateDTO) throws RegraDeNegocioException {
        //validando se o login já está registrado
        usuarioService.existsLogin(companhiaCreateDTO.getLogin());
        //validando se o cpnj já está registrado
        validCnpj(companhiaCreateDTO.getCnpj());

        //editando e adicionando usuario ao comprador
        CompanhiaEntity companhiaEntity = objectMapper.convertValue(companhiaCreateDTO, CompanhiaEntity.class);
        companhiaEntity.setTipoUsuario(TipoUsuario.COMPANHIA);
        companhiaEntity.setSenha(passwordEncoder.encode(companhiaCreateDTO.getSenha()));
        companhiaEntity.setAtivo(true);
        companhiaEntity.setCargos(new HashSet<>());
        companhiaEntity.getCargos().add(cargoService.findByNome("ROLE_COMPANHIA"));

        //salvando no bd o novo comprador
        companhiaRepository.save(companhiaEntity);
        cargoService.saveCargo(companhiaEntity);
//        emailService.sendEmail(companhiaEntity);
        return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);
    }

    public CompanhiaDTO update(CompanhiaUpdateDTO companhiaUpdateDTO) throws RegraDeNegocioException {
        //Retorna o companhia
        CompanhiaEntity companhiaEntity = getCompanhiaSemId();

        if(companhiaUpdateDTO.getSenha().equals(companhiaUpdateDTO.getSenha().trim())){
            throw new RegraDeNegocioException("Senha idêntica! Informe uma senha diferente.");
        }

        //alterando entidade
        companhiaEntity.setSenha(passwordEncoder.encode(companhiaUpdateDTO.getSenha()));
        companhiaEntity.setNomeFantasia(companhiaUpdateDTO.getNomeFantasia());

        //salvando no bd
        companhiaRepository.save(companhiaEntity);

        return objectMapper.convertValue(companhiaEntity, CompanhiaDTO.class);
    }

    public void delete(Integer id, String cnpj) throws RegraDeNegocioException {
        //procurando companhia pelo ID
        CompanhiaEntity companhia = getCompanhiaComId(id);

        //deletando companhia do bd
        if(companhia.getCnpj().trim().equals(cnpj.trim())){
            usuarioService.deleteById(companhia.getIdUsuario());
        } else {
            throw new RegraDeNegocioException("CNPJ Inválido!");
        }
    }

    public CompanhiaDTO getByCompanhia() throws RegraDeNegocioException {
        return objectMapper.convertValue(getCompanhiaSemId(), CompanhiaDTO.class);
    }

    protected CompanhiaEntity recuperarCompanhia(String param, Integer idPassagem){
        return companhiaRepository.findSingleResultByParamAndValue(param, idPassagem);
    }

    protected void validCnpj(String cnpj) throws RegraDeNegocioException {
        if (companhiaRepository.existsCompanhiaEntityByCnpjIsContaining(cnpj)) {
            throw new RegraDeNegocioException("Este CNPJ já está cadastrado!");
        }
    }

    protected CompanhiaEntity getCompanhiaSemId() throws RegraDeNegocioException {
        return companhiaRepository.findById(usuarioService.getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada"));
    }


    protected CompanhiaEntity getCompanhiaComId(Integer id) throws RegraDeNegocioException {
        return companhiaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Companhia não encontrada"));
    }

}
