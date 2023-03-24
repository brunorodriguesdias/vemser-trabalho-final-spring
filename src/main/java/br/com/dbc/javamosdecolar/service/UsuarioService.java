package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.LoginDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.entity.CargoEntity;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final CargoService cargoService;

    public UsuarioDTO create(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);

        StandardPasswordEncoder standardPasswordEncoder = new StandardPasswordEncoder();
        String senha = standardPasswordEncoder.encode(usuarioCreateDTO.getSenha());
        usuarioEntity.setSenha(senha);
        usuarioEntity.setTipoUsuario(TipoUsuario.ADMIN);
        usuarioEntity.setCargos(new HashSet<>());
        usuarioEntity.getCargos().add(cargoService.findByNome("ROLE_ADMIN"));
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
