package com.portfolio.raven.dto.userDto;

import com.portfolio.raven.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserList(UUID id, String username, String email, Boolean status, Instant created_at, Instant update_at) {

     public  UserList(User user){
         this(user.getId(), user.getDisplayUsername(), user.getEmail(), user.getStatus(), user.getCreated_at(), user.getUpdate_at());
     }

}
