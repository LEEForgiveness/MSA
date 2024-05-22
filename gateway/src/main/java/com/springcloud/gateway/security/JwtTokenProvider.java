package com.springcloud.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

	private final Environment env;

	@Value("${JWT.SECRET_KEY}")
	private String SECRET;

	private Claims getClaimsFromJwtToken(String token) {
		try {
			return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	public Date getExpiredTime(String token) {
		return getClaimsFromJwtToken(token).getExpiration();
	}

	public void validateJwtToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
		} catch (SignatureException | MalformedJwtException |
				 UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException jwtException) {
			throw jwtException;
		}
	}
}
