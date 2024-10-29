/*
 * Date create 30/06/2020.
 */
package com.manager.systems.web.api.common.security.jwt;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.manager.systems.web.api.common.constants.CommonConstants;
import com.manager.systems.web.api.common.constants.ParameterConstants;
import com.manager.systems.web.api.login.domain.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    // Chave com algoritmo HS512
    // http://www.allkeysgenerator.com
    private static final String JWT_SECRET = "PdSgVkYp3s6v9y/B?E(H+MbQeThWmZq4t7w!z%C*F)J@NcRfUjXn2r5u8x/A?D(G";

    public static Claims getClaims(String token) {
        final byte[] signingKey = JwtUtil.JWT_SECRET.getBytes();
        token = token.replace(ParameterConstants.PARAMETER_BEARER, CommonConstants.BLANK);
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token).getBody();
    }

    public static String getLogin(final String token) {
        final Claims claims = getClaims(token);
        if (!isNull(claims)) {
            return claims.getSubject();
        }
        return null;
    }

    public static List<GrantedAuthority> getRoles(final String token) {
        final Claims claims = getClaims(token);
        if (claims == null) {
            return null;
        }
        return ((List<?>) claims
                .get(ParameterConstants.PARAMETER_ROLES)).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());
    }

    public static boolean isTokenValid(final String token) {
        final Claims claims = getClaims(token);
        if (nonNull(claims)) {
            final String login = claims.getSubject();
            final Date expiration = claims.getExpiration();
            final Date now = new Date(System.currentTimeMillis());
            return login != null && expiration != null && now.before(expiration);
        }
        return false;
    }

    public static String createToken(final UserDetails user, final Long id) {
        final List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final byte[] signingKey = JwtUtil.JWT_SECRET.getBytes();

        final int days = 1;
        final long time = days * 24 /*horas*/ * 60 /*min*/ * 60 /*seg*/ * 1000  /*milis*/;
        final Date expiration = new Date(System.currentTimeMillis() + time);
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setSubject(user.getUsername())
                .setExpiration(expiration)
                .claim(ParameterConstants.PARAMETER_ID, id)
                .claim(ParameterConstants.PARAMETER_ROLES, roles)
                .compact();
    }
    
    public static Long getAuthId() {
        final User user = (User) getUserDetails();
        if(user != null){
            return user.getId();
        }
        return null;
    }

    public static String getAuthLogin() {
        final UserDetails user = getUserDetails();
        if(user != null){
            return user.getUsername();
        }
        return null;
    }

    public static UserDetails getUserDetails(){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() != null){
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }
}
