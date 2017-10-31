package vsg.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Denis Orlov.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	final private CustomAuthenticationEntryPoint mAuthenticationEntryPoint;

	@Autowired
	public SecurityConfig(CustomAuthenticationEntryPoint mAuthenticationEntryPoint) {
		this.mAuthenticationEntryPoint = mAuthenticationEntryPoint;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/secure-sample/**").authenticated().and().httpBasic()
				.authenticationEntryPoint(mAuthenticationEntryPoint);
		http.authorizeRequests().antMatchers("/sample/**").permitAll();
		http.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("password").roles("SECURE_USER");
	}
}
