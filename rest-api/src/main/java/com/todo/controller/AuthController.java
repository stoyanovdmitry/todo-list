package com.todo.controller;

import com.todo.entity.RefreshToken;
import com.todo.entity.User;
import com.todo.repository.RefreshTokenRepository;
import com.todo.security.jwt.JwtConstants;
import com.todo.security.jwt.JwtGenerator;
import com.todo.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/token")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenRepository tokenRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/refresh")
    public void refreshAccessToken(HttpServletRequest req, HttpServletResponse res) {
        String header = req.getHeader(JwtConstants.JWT_HEADER);

        if (header == null || !header.startsWith(JwtConstants.JWT_PREFIX)) {
            throw new UnsupportedJwtException("Wrong refresh token. You need to login again");
        }

        String token = header.replace(JwtConstants.JWT_PREFIX, "");

        Claims body = Jwts.parser()
                          .setSigningKey(JwtConstants.REFRESH_SECRET.getBytes())
                          .parseClaimsJws(token)
                          .getBody();

        RefreshToken existToken = tokenRepository.findByToken(token);
        if (existToken == null) {
            throw new UnsupportedJwtException("Wrong refresh token. You need to login again");
        }

        String username = body.getSubject();

        User user = userService.getUserIfPresent(username);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));

        String accessToken = JwtGenerator.getAccessToken(authentication);
        String refreshToken = JwtGenerator.getRefreshToken(authentication);

        res.addHeader(JwtConstants.ACCESS_HEADER, JwtConstants.JWT_PREFIX + accessToken);
        res.addHeader(JwtConstants.REFRESH_HEADER, JwtConstants.JWT_PREFIX + refreshToken);

        tokenRepository.delete(existToken.getId());
        tokenRepository.save(new RefreshToken(user, refreshToken));
    }
}
