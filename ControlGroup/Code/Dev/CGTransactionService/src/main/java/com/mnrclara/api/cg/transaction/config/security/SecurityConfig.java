package com.mnrclara.api.cg.transaction.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mnrclara.api.cg.transaction.service.UserAuthService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserAuthService userLoginService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userLoginService).passwordEncoder(encoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.cors().and()
	      	.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER);
	}
	
//	 @Bean
//     public CORSFilter corsFilter() {
//         CorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();
//         config.addAllowedOrigin("http://localhost:8080");
//         config.addAllowedMethod(HttpMethod.DELETE);
//         config.addAllowedMethod(HttpMethod.GET);
//         config.addAllowedMethod(HttpMethod.OPTIONS);
//         config.addAllowedMethod(HttpMethod.PUT);
//         config.addAllowedMethod(HttpMethod.POST);
//         ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**", config);
//         return new CORSFilter(source);
//     }
//
   @Override
   public void configure(WebSecurity web) throws Exception {
       web.ignoring().antMatchers(
    		   "/v2/api-docs",
    		   "/swagger-resources/**",
               "/swagger-ui.html",
               "/webjars/**"
    		   );
   }

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
