package com.pavan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pavan.service.CustomUserDetailsService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	private PasswordEncoder passwordEncoder() {
		System.out.println(new BCryptPasswordEncoder().encode("java"));
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(HttpSecurity security) throws Exception {
		security.csrf().disable();
		security.authorizeRequests().antMatchers("/*").authenticated().antMatchers("/static/*").permitAll()
				.antMatchers("/admin/*").access("hasRole('ADMIN')").antMatchers("/user/*")
				.access("hasAnyRole('USER','ADMIN')").and().formLogin().loginPage("/login").permitAll().and().logout()
				.permitAll().and().exceptionHandling().accessDeniedPage("/403");
	}

}
