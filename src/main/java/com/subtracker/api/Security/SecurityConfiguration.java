package com.subtracker.api.Security;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    @Autowired
    private  JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private  AuthenticationProvider authenticationProvider;


//    @Bean
//    public static PasswordEncoder myPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager(){
//        UserDetails user = User.withUsername("user")
//                .password(myPasswordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                //enable CORS support
//                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()
                        .requestMatchers("/api/auth/current").authenticated()
                        .requestMatchers("/api/permissions/add").authenticated()
                        .requestMatchers("/api/permissions/update").authenticated()
                        .requestMatchers("/api/permissions/delete").authenticated()
                        .requestMatchers("/api/permissions/contexts").authenticated()
                        .requestMatchers("/api/permissions/guests").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/subscriptions").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/subscriptions").hasAnyAuthority("OWNER", "GUEST_RW")
                        .requestMatchers(HttpMethod.PUT, "/api/subscriptions").hasAnyAuthority("OWNER", "GUEST_RW")
                        .requestMatchers(HttpMethod.DELETE, "/api/subscriptions/**").hasAnyAuthority("OWNER", "GUEST_RW")
                        .requestMatchers(HttpMethod.GET, "/api/subscriptions/**").authenticated()

                        .requestMatchers("/api-docs").permitAll()
                        .requestMatchers("/swagger-ui/*").permitAll()
                        .requestMatchers("/api/subscriptions/**").permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//        return tomcat;
//    }
//    private Connector createStandardConnector() {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setPort(8080);
//        return connector;
//    }
}