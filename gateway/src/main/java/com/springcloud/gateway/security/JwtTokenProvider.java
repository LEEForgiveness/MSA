package com.springcloud.gateway.security;

import com.springcloud.gateway.exception.CustomException;
import com.springcloud.gateway.exception.ResponseStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
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
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			String tokenType = claimsJws.getBody().get("TokenType", String.class);
			log.info("tokenType: {}", tokenType);
			if("refresh".equals(tokenType)) {
				throw new CustomException(ResponseStatus.JWT_FAIL_WITH_REFRESH);
			}
		} catch (SignatureException exception) {
			throw new CustomException(ResponseStatus.TOKEN_NOT_VALID);
		} catch (MalformedJwtException exception) {
			throw new CustomException(ResponseStatus.JWT_VALID_FAILED);
		} catch (ExpiredJwtException exception) {
			throw new CustomException(ResponseStatus.EXPIRED_AUTH_CODE);
		} catch (UnsupportedJwtException exception) {
			throw new CustomException(ResponseStatus.TOKEN_NULL);
		} catch (IllegalArgumentException exception) {
			throw new CustomException(ResponseStatus.JWT_VALID_FAILED);
		}
	}
}
