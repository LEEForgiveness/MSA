package com.eurekaclient.userservice.common.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	//    private final JwtAuthenticationFilter jwtTokenProvider;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			var cors = new org.springframework.web.cors.CorsConfiguration();
			cors.setAllowedOriginPatterns(List.of("*"));
			cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			cors.setAllowedHeaders(List.of("*"));
			return cors;
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.csrf(CsrfConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.httpBasic(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(
				authorizeHttpRequests -> authorizeHttpRequests
					// 허용 범위
					.requestMatchers("**")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.sessionManagement(
				sessionManagement -> sessionManagement
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authenticationProvider(
				authenticationProvider); //등록할때 사용하는 키는 authenticationProvider를 사용
//            .addFilterBefore(jwtTokenProvider, UsernamePasswordAuthenticationFilter.class); //내가 만든 필터 추가

		return http.build();
	}
}
