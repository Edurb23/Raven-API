package com.portifolio.Raven.controller;


import com.portifolio.Raven.dto.userDto.*;
import com.portifolio.Raven.repository.UserRepository;
import com.portifolio.Raven.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public ResponseEntity<List<UserList>> listAll(Pageable pageable){
        var ListUser = userService.listAll(pageable).stream().toList();
        return  ok(ListUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDetail> getByID(@PathVariable("id")UUID id){
        var userDetail = userService.getById(id);
        return ok(userDetail);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetail> getCurrentUser(){
        var userCurrent = userService.getCurrentUser();
        return ResponseEntity.ok(userCurrent);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetail> post(@RequestBody @Valid RegisterUserDto dto, UriComponentsBuilder builder){
        UserDetail userDetail = userService.create(dto);
        var uri = builder.path("/user/{id}").buildAndExpand(userDetail.id()).toUri();
        return ResponseEntity.created(uri).body(userDetail);
    }

    @PutMapping("/me/username")
    public ResponseEntity<UserDetail> updateUsername(@RequestBody @Valid UpdateUsernameDto dto){
        UserDetail updateUser = userService.updateUsername(dto);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/me/email")
    public ResponseEntity<UserDetail> updateEmail(@RequestBody @Valid UpdateEmailDto dto){
        UserDetail updateEmail = userService.updateEmail(dto);
        return ResponseEntity.ok(updateEmail);
    }

    @PutMapping("/me/password")
    public ResponseEntity<UserDetail> updatePassword(@RequestBody @Valid UpdatePassword dto){
        UserDetail updatePassowrd = userService.updatePassword(dto);
        return ResponseEntity.ok(updatePassowrd);
    }

}
