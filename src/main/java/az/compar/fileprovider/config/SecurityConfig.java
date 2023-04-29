package az.compar.fileprovider.config;

import az.compar.fileprovider.service.UserService;
import az.compar.fileprovider.util.exceptions.UserNotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            try {
                return userService.getByEmail(email);
            } catch (UserNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        }).passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/users/register", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.getWriter().write(authException.getMessage());
                }).accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(403);
                    response.getWriter().write(accessDeniedException.getMessage());
                })
                .and()
                .formLogin()
                .successHandler(((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("Authentication successful");
                }))
                .failureHandler(((request, response, exception) -> {
                    response.setStatus(401);
                    response.getWriter().write(exception.getMessage());
                })).usernameParameter("email")
                .and()
                .logout()
                .logoutSuccessHandler(((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("Logout was successful");
                }))
        ;
    }
}
