package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.token.AuthDto;
import com.portifolio.Raven.dto.token.TokenDto;
import com.portifolio.Raven.entity.User;
import com.portifolio.Raven.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public TokenDto auth(AuthDto authDto){
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                authDto.email(),
                authDto.password()
        );
        var authentication = authenticationManager.authenticate(authenticationToken);
        var usuario = (User) authentication.getPrincipal();
        var token = tokenService.generateToken(usuario);
        return new TokenDto(token);
    }



}
