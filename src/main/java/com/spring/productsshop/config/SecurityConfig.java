package com.spring.productsshop.config;

/*
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.authentication.builders.
 * AuthenticationManagerBuilder; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.annotation.web.configuration.
 * WebSecurityConfiguration; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig extends
 * WebSecurityConfigurerAdapter {
 * 
 * @Override protected void configure(HttpSecurity http) throws Exception { http
 * .csrf().disable() // Deshabilitar CSRF para simplificar, habilítalo en
 * producción
 * 
 * .antMatchers( "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
 * "/swagger-resources/**", "/webjars/**", "/api/login" ).permitAll() //
 * Permitir acceso sin autenticación a las rutas de Swagger y al endpoint de
 * login .anyRequest().authenticated() // Requerir autenticación para cualquier
 * otra solicitud .and() .formLogin() .loginPage("/login") // Personaliza tu
 * página de login si tienes una .permitAll() .and() .logout() .permitAll(); }
 * 
 * protected void configure(AuthenticationManagerBuilder auth) throws Exception
 * { auth.inMemoryAuthentication()
 * .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN")
 * ;
 * 
 * }
 * 
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); } }
 */




