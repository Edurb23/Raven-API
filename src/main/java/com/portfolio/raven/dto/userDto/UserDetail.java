package com.portfolio.raven.dto.userDto;

import com.portfolio.raven.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDetail(UUID id, String username, String email, Boolean status, Instant created_at, Instant update_at,
                         java.util.Set<String> roles) {

    public UserDetail(User user){
        this(user.getId(), user.getDisplayUsername(), user.getEmail(), user.getStatus(), user.getCreated_at(), user.getUpdate_at(),
                user.getRoles() == null ? java.util.Set.of() : user.getRoles().stream().map(com.portfolio.raven.entity.Role::getName).collect(java.util.stream.Collectors.toSet()));
    }

}
