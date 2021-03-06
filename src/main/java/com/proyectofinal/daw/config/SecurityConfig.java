package com.proyectofinal.daw.config;

import com.proyectofinal.daw.services.UsuarioDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("usuarioDetailsService")
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    public void configureglobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioDetailsService).passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/js/**", "/css/**", "/img/**").permitAll()
                .antMatchers("/").permitAll().antMatchers("/form/registro").permitAll().antMatchers("/registro")
                .permitAll().antMatchers("/auth/**").authenticated().anyRequest().authenticated().and().formLogin()
                .loginPage("/").loginProcessingUrl("/form/login").defaultSuccessUrl("/index", true).permitAll().and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll();
    }

}
