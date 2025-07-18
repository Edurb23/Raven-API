package com.portifolio.Raven.dto.userDto;

import com.portifolio.Raven.entity.Role;
import com.portifolio.Raven.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserList(UUID id, String username, String email, Boolean status , Set<Role> roles , Instant created_at, Instant update_at) {

     public  UserList(User user){
         this(user.getId(),user.getUsername(),user.getEmail(),user.getStatus(), user.getRoles() ,user.getCreated_at(),user.getUpdate_at());
     }

}
