package ismaelTortosa.diceGame.model.security.configuration;

import ismaelTortosa.diceGame.model.domain.AdminEntity;
import ismaelTortosa.diceGame.model.security.filters.JWTAuthenticationFilter;
import ismaelTortosa.diceGame.model.security.filters.JWTAuthorizationFilter;
import ismaelTortosa.diceGame.model.security.users.ManagementDetailServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig  {

    private final ManagementDetailServiceImpl managementDetailService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public AdminEntity adminEntity() {
        return new AdminEntity();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();

        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/admins/add").permitAll()
                .requestMatchers("/players/add").permitAll()
                .requestMatchers("/players/getAll").permitAll()
                .requestMatchers("/players/getAllUp").permitAll()
                .requestMatchers("/players/getAllDown").permitAll()
                .requestMatchers("/players/getUp").permitAll()
                .requestMatchers("/players/getDown").permitAll()
                .requestMatchers("/players/getAverage").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(managementDetailService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

}

