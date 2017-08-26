package org.tenbitworks.configuration;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String CLAZZ = WebSecurityConfig.class.getName();
	private static final Logger LOGGER = Logger.getLogger(CLAZZ);
	
	@Autowired
	DataSource dataSource;
	JdbcUserDetailsManager userDetailsManager;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.requiresChannel()
				.anyRequest()
				.requiresSecure()
				.and()
			.csrf()
				.requireCsrfProtectionMatcher(getCSRFRequestMatcher())
				.and()
			.authorizeRequests()
				.antMatchers( "/webjars/**", "/css/**", "/script/**").permitAll()
				.antMatchers("/api/**").hasRole("API")
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
			.httpBasic()
				.and()
			.rememberMe();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> service = auth.jdbcAuthentication();
    	
    	PasswordEncoder passwordEncoder = getPasswordEncoder();
    	
    	userDetailsManager = service
        	.dataSource(dataSource)
        	.passwordEncoder(passwordEncoder)
        	.getUserDetailsService();
    	
   		if (!dataSource.getConnection().prepareStatement("select 1 from users").executeQuery().next()) {
   			LOGGER.info("No users found, adding defaults");

			service.withUser("user").password(passwordEncoder.encode("user")).roles("USER");
			service.withUser("admin").password(passwordEncoder.encode("admin")).roles("USER", "ADMIN", "API");
   		} else {
   			LOGGER.info("Users found");
   		}
    }
    
    @Bean
    public JdbcUserDetailsManager getUserDetailsManager() {
        return userDetailsManager;
    }
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestMatcher getCSRFRequestMatcher() {
    	return new RequestMatcher() {
    		private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    		private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/api/*", null);

    		@Override
    		public boolean matches(HttpServletRequest request) {
    			if(allowedMethods.matcher(request.getMethod()).matches()){
    				return false;
    			}
    			return !unprotectedMatcher.matches(request);
    		}
    	};
    }
}