package com.SBProject.DivaDrop.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Autowired
    @Lazy
    private AuthFailureHandlerImpl authFailureHandler;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
// not required due to @Service
//    public UserDetailsService userDetailsService(){
//        return new UserDetailServiceImpl();
//    }
    @Autowired
    private AuthSuccessHandlerImpl authSuccessHandler;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req->req
                        .requestMatchers("/DivaDrop/user/save_user", "/DivaDrop/register", "/DivaDrop/signin").permitAll()
                        .requestMatchers("/DivaDrop/user/**").hasRole("USER")
                .requestMatchers("/DivaDrop/admin/**").hasRole("ADMIN")
                        .requestMatchers("/**").permitAll())
                .formLogin(form->form.loginPage("/DivaDrop/signin")
                        .loginProcessingUrl("/login")
//                        .defaultSuccessUrl("/DivaDrop/"))
                                .failureHandler(authFailureHandler)
                                .successHandler(authSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/DivaDrop/signin?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );


        return http.build();
    }

}
