package ru.otus.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .jdbcAuthentication().dataSource(dataSource).withDefaultSchema()
                .withUser(User.withUsername("admin")
                        .password(passwordEncoder().encode("password"))
                        .roles("ADMIN"))
                .withUser(User.withUsername("librarian")
                        .password(passwordEncoder().encode("password"))
                        .roles("LIBRARIAN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/registration", "/error-500").permitAll()
                .antMatchers("/").authenticated()
                .antMatchers( "/author", "/book", "/genre").hasAnyRole("ADMIN", "LIBRARIAN", "USER")
                .antMatchers("/search/**", "/comment/**").hasRole("USER")
                .antMatchers("/book/**", "/author/**", "/genre/**").hasRole("LIBRARIAN")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
}
