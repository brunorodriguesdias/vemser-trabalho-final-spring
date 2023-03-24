package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.LoginDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final AuthenticationManager authenticaticationManager;

    private final ObjectMapper objectMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, @Lazy AuthenticationManager authenticaticationManager, ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.authenticaticationManager = authenticaticationManager;
        this.objectMapper = objectMapper;
    }

    public UsuarioDTO create(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);

        StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();
        String senha = standardPasswordEncoder.encode(usuarioCreateDTO.getSenha());
        usuarioEntity.setSenha(senha);
        usuarioEntity.setTipoUsuario(TipoUsuario.ADMIN);
        usuarioEntity.setAtivo(true);

        usuarioRepository.save(usuarioEntity);

        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }

    public void deleteById(Integer idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    public void existsLogin(String login) throws RegraDeNegocioException {
        if(usuarioRepository.existsByLogin(login)){
            throw new RegraDeNegocioException("Este login já está cadastrado!");
        }
    }

    public UsuarioEntity autenticar(LoginDTO loginDTO) throws RegraDeNegocioException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getLogin(),
                        loginDTO.getSenha());

        Authentication authentication;
        try {
            authentication =
                    authenticaticationManager.authenticate(usernamePasswordAuthenticationToken);

            Object principal = authentication.getPrincipal();
            UsuarioEntity usuarioEntity = (UsuarioEntity) principal;

            return usuarioEntity;
        } catch (BadCredentialsException ex) {
            throw new RegraDeNegocioException("Usuario não encontrado");
        }
    }

    public Integer getIdLoggedUser() {
        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }

    public Optional<UsuarioEntity> findByLogin (String login) {
        return usuarioRepository.findByLogin(login);
    }
}
