package com.eurekaclient.userservice.common.security;

import com.eurekaclient.userservice.common.exception.CustomException;
import com.eurekaclient.userservice.common.exception.ResponseStatus;
import com.eurekaclient.userservice.infrastructure.MemberRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final MemberRepository memberRepository;

	@Bean
	public UserDetailsService memberDetailsService() {
		return uuid -> memberRepository.findByUuid(uuid)
			.map(member -> new User(
				member.getUuid(),
				"",
				new ArrayList<>()
			))
			.orElseThrow(
				() -> new CustomException(ResponseStatus.USER_NOT_FOUND));
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(memberDetailsService());
		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
