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


    @PostMapping("/register")
    public ResponseEntity<UserDetail> post(@RequestBody @Valid RegisterUserDto dto, UriComponentsBuilder builder){
        UserDetail userDetail = userService.create(dto);
        var uri = builder.path("/user/{id}").buildAndExpand(userDetail.id()).toUri();
        return ResponseEntity.created(uri).body(userDetail);
    }



    @PutMapping("/{id}/email")
    public ResponseEntity<UserDetail> updateEmail(@PathVariable("id") UUID id,
                                                  @RequestBody @Valid UpdateEmailDto dto){
        UserDetail updated = userService.updateEmail(id, dto);
        return ResponseEntity.ok(updated);

    }


    @PutMapping("/{id}/username")
    public ResponseEntity<UserDetail> updateUsername(@PathVariable("id") UUID id,
                                                     @RequestBody @Valid UpdateUsernameDto dto){
        UserDetail updated = userService.updateUsername(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }



   /*
     //   @GetMapping("/me")
 //   public ResponseEntity<UserDetail> getCurrentUser(){
    //        var userCurrent = userService.getCurrentUser();
    //        return ResponseEntity.ok(userCurrent);
    //    }






    @PutMapping("/me/password")
    public ResponseEntity<UserDetail> updatePassword(@RequestBody @Valid UpdatePassword dto){
        UserDetail updatePassowrd = userService.updatePassword(dto);
        return ResponseEntity.ok(updatePassowrd);
    }*/

}
