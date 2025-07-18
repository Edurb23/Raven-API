package com.portifolio.Raven.dto.userDto;

import com.portifolio.Raven.entity.Role;
import com.portifolio.Raven.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserDetail(UUID id, String username, String email, Boolean status ,Set<Role>roles ,Instant created_at, Instant update_at){

    public UserDetail(User user){
        this(user.getId(),user.getUsername(),user.getEmail(),user.getStatus(), user.getRoles() ,user.getCreated_at(),user.getUpdate_at());
    }

}
