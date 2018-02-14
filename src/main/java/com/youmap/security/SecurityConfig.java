package com.youmap.security;
 
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
     
    @Autowired
    DataSource dataSource;

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.httpBasic().and().authorizeRequests()
    	.antMatchers("/access/register").permitAll()
    	.antMatchers("/login").permitAll()
    	.antMatchers("/js/**").permitAll()
    	.antMatchers("/css/**").permitAll()
    	.antMatchers("/travelViewer/**").permitAll()
    	.anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("/")
				.permitAll()
		.and()
			.rememberMe()
				.key("uniqueAndSecret")
				.rememberMeCookieName("javasampleapproach-remember-me")
				.tokenValiditySeconds(24 * 60 * 60)
		.and()
			.logout()
			.deleteCookies("JSESSIONID")
			.permitAll()
			.and().csrf().disable();
//        http
//            .authorizeRequests()
//                .anyRequest().authenticated() 
//            .and()
//            .formLogin()
//            	.loginPage("/")
//            		.permitAll()
//            	.loginProcessingUrl("/processlogin")
//                    .permitAll()
//                .and()
//                .logout()
//                    .logoutUrl("/logmeout")
//                    	.logoutSuccessUrl("/")
//                        	.permitAll()
//                        .and().requestCache().requestCache(new NullRequestCache())
//                        .and().httpBasic();
//                        .and().csrf().disable();
    }
    
    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
        .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("Select username, password, enabled From users Where username=?")
                .authoritiesByUsernameQuery("Select username, authority From users Where username=?")
                .passwordEncoder(passwordEncoder());
    }
     
}