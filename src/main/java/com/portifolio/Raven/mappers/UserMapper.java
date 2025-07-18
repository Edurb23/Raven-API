package com.portifolio.Raven.mappers;

import com.portifolio.Raven.dto.artistDto.RegisterArtistDto;
import com.portifolio.Raven.dto.userDto.*;
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

    public UserList toList(User user){
        return new UserList(user);
    }

    public User updateUsername(User user, UpdateUsernameDto dto){
        user.setUsername(dto.Newusername());
        return user;
    }

    public User updateEmail(User user, UpdateEmailDto dto){
        user.setEmail(dto.newemail());
        return user;
    }

    public User updatePassword(User user, String newpassword ){
        user.setPassword(newpassword);
        return user;
    }



}
