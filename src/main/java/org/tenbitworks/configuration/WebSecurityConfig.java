package org.tenbitworks.configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String CLAZZ = WebSecurityConfig.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	@Autowired
	DataSource dataSource;
	JdbcUserDetailsManager userDetailsManager;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers( "/webjars/**", "/css/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll()
				.deleteCookies("JSESSIONID")
				.and()
			.rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> service = auth.jdbcAuthentication();
    	
    	userDetailsManager = service
        	.dataSource(dataSource)
        	.passwordEncoder(new BCryptPasswordEncoder())
        	.getUserDetailsService();
    	
    	try {
	   		if (!dataSource.getConnection().prepareStatement("select 1 from users").executeQuery().next()) {
	   			LOGGER.info("No users found, adding defaults");

				service.withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER");
				service.withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");
	   		} else {
	   			LOGGER.info("Users found");
	   		}
    	} catch (Exception e) {
    		LOGGER.logp(Level.SEVERE, CLAZZ, "configureGlobal", "Exception caught when checking for users", e);
    		LOGGER.info("Creating default user schema and users");
    		
    		service.withDefaultSchema();
    		service.withUser("user").password(new BCryptPasswordEncoder().encode("user")).roles("USER");
			service.withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("USER", "ADMIN");
    	}
    }
    
    @Bean
    public JdbcUserDetailsManager getUserDetailsManager() {
        return userDetailsManager;
    }
}