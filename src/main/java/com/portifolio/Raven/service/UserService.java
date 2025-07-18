package com.portifolio.Raven.service;

import com.portifolio.Raven.dto.userDto.*;
import com.portifolio.Raven.entity.User;
import com.portifolio.Raven.exceptions.EmailAlreadyExistsException;
import com.portifolio.Raven.exceptions.UsernameAlreadyExistExceotion;
import com.portifolio.Raven.mappers.UserMapper;
import com.portifolio.Raven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public List<UserList> listAll(Pageable pageable){
        return userRepository.findAll(pageable)
                .stream()
                .map(userMapper::toList)
                .toList();
    }

    public UserDetail getById(UUID id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genero não encontrado"));
        return userMapper.userDetail(user);
    }

    public UserDetail getCurrentUser(){
        var principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.userDetail(principal);
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public UserDetail updateUsername(UpdateUsernameDto dto){
        User user = getCurrentAuthenticatedUser();
        userMapper.updateUsername(user, dto);
        userRepository.save(user);
        return new UserDetail(user);
    }

    public UserDetail updateEmail(UpdateEmailDto dto){
        User user = getCurrentAuthenticatedUser();

        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Senha atual incorreta");
        }
        userMapper.updateEmail(user, dto);
        userRepository.save(user);
        return new UserDetail(user);

    }


    public UserDetail updatePassword(UpdatePassword dto){
        User user = getCurrentAuthenticatedUser();

        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Senha atual Incorreta");
        }
        String newPassoword = passwordEncoder.encode(dto.newPassoword());
        userMapper.updatePassword(user, newPassoword);
        userRepository.save(user);
        return new UserDetail(user);

    }




}
