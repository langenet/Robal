package ac.project.Robal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ac.project.Robal.services.AccountDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final AccountDetailService userDetailsService;

	@Autowired
	public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, AccountDetailService userDetailsService) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/h2-console/**").permitAll()
//				.antMatchers(HttpMethod.POST, "/admins/**").permitAll()
				.antMatchers(HttpMethod.POST, "/owners/**").permitAll()
				.antMatchers(HttpMethod.POST, "/customers/**").permitAll()
//				.antMatchers(HttpMethod.GET, "/stores/**").permitAll()
				.antMatchers(HttpMethod.GET, "/store-products").permitAll()
				.antMatchers("/customers/**").authenticated()
				.antMatchers("/owners/**").authenticated()
				.antMatchers("/admins/**").authenticated()
				.antMatchers("/stores/**").authenticated()
				.antMatchers("/orders/**").authenticated()
				.antMatchers("/store-products/**").authenticated()
				.antMatchers("/order-products/**").authenticated()
				.antMatchers("/products/**").authenticated()
				.and()
				.headers().frameOptions().sameOrigin()
				.and()
				.httpBasic()
				.and()
				.formLogin()
				.and()
				.csrf()
				.disable()
				.logout();

	}
}

