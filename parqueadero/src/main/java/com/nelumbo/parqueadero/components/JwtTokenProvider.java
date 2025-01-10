package com.nelumbo.parqueadero.components;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.nelumbo.parqueadero.entities.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private static final long JWT_EXPIRATION_TIME = 21600000L;
	private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public String generateToken(Authentication authentication) {
		var principal = (Usuario) authentication.getPrincipal();
		String email = principal.getUsername();
		String rol = principal.getAuthorities().stream().findFirst()
				.orElseThrow(() -> new IllegalStateException("El usuario no tiene roles")).getAuthority()
				.replace("ROLE_", "");

		return Jwts.builder().setSubject(email).claim("rol", rol).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME)).signWith(secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

		String email = claims.getSubject();
		String rol = claims.get("rol", String.class);

		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);
		User principal = new User(email, "", List.of(authority));

		return new UsernamePasswordAuthenticationToken(principal, token, List.of(authority));
	}

	public String getEmailFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
}
