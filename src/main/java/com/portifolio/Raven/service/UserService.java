package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.userDto.RegisterUserDto;
import com.portifolio.Raven.dto.userDto.UserDetail;
import com.portifolio.Raven.entity.User;
import com.portifolio.Raven.exceptions.EmailAlreadyExistsException;
import com.portifolio.Raven.exceptions.UsernameAlreadyExistExceotion;
import com.portifolio.Raven.mappers.UserMapper;
import com.portifolio.Raven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserDetail create(RegisterUserDto dto){
        if(userRepository.existsByEmail(dto.email())){
            throw new EmailAlreadyExistsException("Esse email já foi cadastrado.");
        }
        if(userRepository.existsByUsername(dto.username())){
            throw new UsernameAlreadyExistExceotion("Esse Username já esta sendo usado");
        }
        String password  = passwordEncoder.encode(dto.password());
        User user = userMapper.toEntity(dto, password);
        userRepository.save(user);
        return userMapper.userDetail(user);
    }



}
