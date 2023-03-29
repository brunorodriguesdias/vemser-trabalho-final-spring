package br.com.dbc.javamosdecolar.service;

import br.com.dbc.javamosdecolar.dto.in.UsuarioCreateDTO;
import br.com.dbc.javamosdecolar.entity.UsuarioEntity;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private CargoService cargoService;


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
        // declaração de variaveis (SETUP)
        UsuarioCreateDTO minhaNovaPessoa = new UsuarioCreateDTO("João Victor",
                LocalDate.of(2000, 9, 8),
                "12345678910",
                "maicon@dbccompany.com.br");
        UsuarioEntity pessoaMockadaDoBanco = getUsuarioEntity();
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setIdPessoa(1);
        pessoaDTO.setNome("João Victor");
        pessoaDTO.setDataNascimento(LocalDate.of(2000, 9, 8));
        pessoaDTO.setEmail("maicon@dbccompany.com.br");
        pessoaDTO.setCpf("12345678910");

        // definindo um comportamento de um método
        // UsuarioEntity pessoaEntity = objectMapper.convertValue(pessoaCreateDTO, UsuarioEntity.class);
        // quando chamar o método xpto com os parametro xxx yyy... retorna o objeto zzz
        when(objectMapper.convertValue(minhaNovaPessoa, UsuarioEntity.class)).thenReturn(pessoaMockadaDoBanco);
        when(pessoaRepository.save(any())).thenReturn(pessoaMockadaDoBanco);

        // objectMapper.convertValue(pessoaEntityCriada, PessoaDTO.class);
        when(objectMapper.convertValue(pessoaMockadaDoBanco, PessoaDTO.class)).thenReturn(pessoaDTO);

        // ação (ACT)
//        PessoaService pessoaService = new PessoaService();
        PessoaDTO pessoaRetornada = pessoaService.create(minhaNovaPessoa);

        // verificar se deu certo / afirmativa  (ASSERT)
        Assertions.assertNotNull(pessoaRetornada);
        Assertions.assertEquals(minhaNovaPessoa.getNome(), pessoaRetornada.getNome());
        Assertions.assertEquals(minhaNovaPessoa.getEmail(), pessoaRetornada.getEmail());
        Assertions.assertEquals(minhaNovaPessoa.getDataNascimento(), pessoaRetornada.getDataNascimento());
        Assertions.assertEquals(minhaNovaPessoa.getCpf(), pessoaRetornada.getCpf());
        Assertions.assertEquals(1, pessoaRetornada.getIdPessoa());
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioMockadoBanco = new UsuarioEntity();
        usuarioMockadoBanco.setIdUsuario(1);
        usuarioMockadoBanco.setNome("João Victor");
        usuarioMockadoBanco.setLogin("joao.victor@email.com");
        usuarioMockadoBanco.setSenha("dsfasffsda");
        return usuarioMockadoBanco;
    }
}

}
