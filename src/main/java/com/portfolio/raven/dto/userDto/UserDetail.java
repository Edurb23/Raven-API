package com.portfolio.raven.dto.userDto;

import com.portfolio.raven.entity.Role;
import com.portfolio.raven.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDetail(UUID id, String username, String email, Boolean status ,Set<Role>roles ,Instant created_at, Instant update_at){

    public UserDetail(User user){
        this(user.getId(),user.getUsername(),user.getEmail(),user.getStatus(), user.getRoles() ,user.getCreated_at(),user.getUpdate_at());
    }

}
