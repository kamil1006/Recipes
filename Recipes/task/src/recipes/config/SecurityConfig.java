package recipes.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {/*     http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();
       */
        http.httpBasic()
                //// enables basic auth.
                .and()
                .authorizeRequests()
                .mvcMatchers("/h2-console/*").permitAll()
                .mvcMatchers("/actuator/shutdown").permitAll()
                .mvcMatchers("/api/recipe/**").hasAnyRole("USER")

                // .antMatchers("/**").hasRole("ADMIN")
                .and()
                .csrf().disable()               //// disabling CSRF will allow sending POST request using Postman
                .headers()
                .frameOptions().disable();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService) // user store 1
                .passwordEncoder(passwordEncoder());


    }


    //-----------------------------------------------------------------------
    //beans
    //bcrypt bean definition
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //-----------------------------------------------------------------------


}
