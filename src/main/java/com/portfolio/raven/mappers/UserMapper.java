package com.portfolio.raven.mappers;

import com.portfolio.raven.dto.userDto.RegisterUserDto;
import com.portfolio.raven.dto.userDto.UpdateEmailDto;
import com.portfolio.raven.dto.userDto.UserDetail;
import com.portfolio.raven.dto.userDto.UserList;
import com.portfolio.raven.entity.User;
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

    public User updateEmail(User user, UpdateEmailDto dto) {
        user.setEmail(dto.email());
        return user;
    }





//    public User updateUsername(User user, UpdateUsernameDto dto){
//        user.setUsername(dto.Newusername());
//        return user;
//    }
//
//    public User updateEmail(User user, UpdateEmailDto dto){
//        user.setEmail(dto.newemail());
//        return user;
//    }
//
//    public User updatePassword(User user, String newpassword ){
//        user.setPassword(newpassword);
//        return user;
//    }



}
