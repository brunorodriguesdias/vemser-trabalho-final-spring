package br.com.dbc.javamosdecolar.secutiry;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable()
                .and().cors()
                .and().csrf().disable()
                .authorizeHttpRequests((requisicao) ->
                        requisicao
                                //All Admin
                                .antMatchers("/voo/all",
                                        "/comprador/all",
                                        "/companhia/all",
                                        "/aviao/all",
                                        "/auth/create",
                                        "/auth/get-usuario-loggado",
                                        "/comprador/deletar/admin",
                                        "/companhia/deletar/admin").hasAuthority("ROLE_ADMIN")

                                //Iguais
                                .antMatchers("/passagem/valor",
                                        "/passagem/buscar/{idPassagem}",
                                        "/passagem/voo",
                                        "/venda/vendas-between")
                                .hasAnyAuthority("ROLE_COMPRADOR", "ROLE_COMPANHIA", "ROLE_ADMIN")

                                //All Comprador
                                .antMatchers("/comprador/**",
                                        "/venda",
                                        "/venda/**/comprador")
                                .hasAnyAuthority("ROLE_COMPRADOR", "ROLE_ADMIN")

                                //All Companhia
                                .antMatchers("/aviao/**",
                                        "/passagem/**",
                                        "/venda/**/companhia",
                                        "/companhia/**",
                                        "/voo/criar",
                                        "/voo/alterar/**",
                                        "/voo/deletar").hasAnyAuthority("ROLE_COMPANHIA", "ROLE_ADMIN")

                                //Iguais mais genericos
                                .antMatchers("/voo/**").hasAnyAuthority("ROLE_COMPANHIA", "ROLE_COMPRADOR", "ROLE_ADMIN")

                                .anyRequest()
                                .authenticated());
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/auth",
                // Qualquer pessoa pode se cadastrar como companhia ou comprador
                "/comprador/create",
                "/companhia/create");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*").allowedMethods("GET", "PUT", "DELETE", "POST");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new StandardPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticaonManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}