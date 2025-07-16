package com.portifolio.Raven.controller;


import com.portifolio.Raven.dto.userDto.RegisterUserDto;
import com.portifolio.Raven.dto.userDto.UserDetail;
import com.portifolio.Raven.repository.UserRepository;
import com.portifolio.Raven.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDetail> post(@RequestBody @Valid RegisterUserDto dto, UriComponentsBuilder builder){
        UserDetail userDetail = userService.create(dto);
        var uri = builder.path("/user/{id}").buildAndExpand(userDetail.id()).toUri();
        return ResponseEntity.created(uri).body(userDetail);
    }



}
