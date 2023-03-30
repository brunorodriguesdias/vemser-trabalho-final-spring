package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.UsuarioDTO;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.entity.enums.TipoUsuario;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private CargoService cargoService;
    @Mock
    private UsuarioRepository usuarioRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);

        ReflectionTestUtils.setField(usuarioService, "passwordEncoder", passwordEncoder);
    }


    @Test // deveCriarComSucesso
    public void shouldCreateWithSucess() throws RegraDeNegocioException {
        // SETUP
        UsuarioCreateDTO minhaNovaPessoa = getUsuarioCreateDTO();
        UsuarioEntity usuarioMockado = getUsuarioEntity();

        /// repository.save() retorna o usuario criado
        when(usuarioRepository.save(any())).thenReturn(usuarioMockado);


        // ação (ACT)
        UsuarioDTO pessoaRetornada = usuarioService.create(minhaNovaPessoa);

        // verificar se deu certo / afirmativa  (ASSERT)
        Assertions.assertNotNull(pessoaRetornada);
        Assertions.assertEquals(minhaNovaPessoa.getNome(), pessoaRetornada.getNome());
        Assertions.assertEquals(minhaNovaPessoa.getLogin(), pessoaRetornada.getLogin());
        Assertions.assertTrue(pessoaRetornada.getAtivo());
        Assertions.assertEquals(pessoaRetornada.getTipoUsuario(), TipoUsuario.ADMIN);
    }

    @Test
    public void shouldDeleteWithSuccess() {
        // SETUP
        Integer idUsuario = 1;

        // ACT
        usuarioService.deleteById(1);

        // ASSERT
        verify(usuarioRepository, times(1)).deleteById(anyInt());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void shouldThrowExceptionIfAlreadyExistLogin () throws RegraDeNegocioException {
        // SETUP
        when(usuarioRepository.existsByLogin(any())).thenReturn(true);

        // ACT
        usuarioService.existsLogin("meuusuario@email.com");
    }

    @Test
    public void shouldReturnIdUsuarioFromContext () {
        // SETUP
        getMockedSecurityContext();

        // ACT
        Integer idUsuarioLogado = usuarioService.getIdLoggedUser();

        // ASSERT
        Assertions.assertEquals(idUsuarioLogado, idUsuarioLogado);
    }

    @Test
    public void shouldReturnUsuarioFromContext() throws RegraDeNegocioException {
        // SETUP
        getMockedSecurityContext();
        UsuarioEntity usuarioLogado = getUsuarioEntity();
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioLogado));

        // ACT
        UsuarioDTO usuarioRetornado = usuarioService.getLoggedUser();

        // ASSERT
        Assertions.assertNotNull(usuarioRetornado);
    }

    @Test
    public void shouldReturnUsuarioOptional() {
        // SETUP
        when(usuarioRepository.findByLogin(anyString()))
                .thenReturn(Optional.of(getUsuarioEntity()));

        // ACT
        Optional<UsuarioEntity> optionalRetornado = usuarioService.findByLogin("meu.usuairo@email.com");

        // ASSERT
        Assertions.assertNotNull(optionalRetornado);
    }

    @Test
    public void shouldReturnCountOfUsers() {
        // SETUP
        when(usuarioRepository.countUsuarioEntitiesBy())
                .thenReturn(1);

        // ACT
        Integer quantidadeDeUsuarios = usuarioService.getCountUsers();

        // ASSERT
        Assertions.assertNotNull(quantidadeDeUsuarios);
        Assertions.assertEquals(1, quantidadeDeUsuarios);
    }

    private static UsuarioCreateDTO getUsuarioCreateDTO() {
        UsuarioCreateDTO minhaNovaPessoa = new UsuarioCreateDTO(
                "carlos.cunha@emai.com", "mypassword", "Carlos Cunha"
        );
        return minhaNovaPessoa;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioMockadoBanco = new UsuarioEntity();
        usuarioMockadoBanco.setIdUsuario(1);
        usuarioMockadoBanco.setNome("Carlos Cunha");
        usuarioMockadoBanco.setLogin("carlos.cunha@emai.com");
//        usuarioMockadoBanco.setSenha("dsfasffsda");
        usuarioMockadoBanco.setAtivo(true);
        return usuarioMockadoBanco;
    }

    private static void getMockedSecurityContext() {
        Integer idUsuarioToken = 1;
        Authentication authentication = Mockito.mock(Authentication.class);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(idUsuarioToken);
    }
}
