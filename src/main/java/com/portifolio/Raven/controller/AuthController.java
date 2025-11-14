package com.portifolio.Raven.controller;


import com.portifolio.Raven.dto.token.AuthDto;
import com.portifolio.Raven.dto.token.TokenDto;
import com.portifolio.Raven.service.AuthService;
import com.portifolio.Raven.service.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class AuthController {


    @Autowired
    private AuthService authService;

   @PostMapping
    public ResponseEntity<TokenDto> login(@RequestBody AuthDto dto){
       var response =  authService.auth(dto);
       return ResponseEntity.ok(response);

   }


}
