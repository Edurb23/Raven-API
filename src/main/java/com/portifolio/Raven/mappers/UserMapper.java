package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.userDto.RegisterUserDto;
import com.portifolio.Raven.dto.userDto.UserDetail;
import com.portifolio.Raven.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterUserDto dto, String password) {
        return new User(dto.username(), dto.email(), password);
    }

    public UserDetail userDetail(User user){
        return new UserDetail(user);
    }

}
