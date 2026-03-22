package com.portfolio.raven.service;

import com.portfolio.raven.dto.userDto.*;
import com.portfolio.raven.entity.User;
import com.portfolio.raven.exceptions.EmailAlreadyExistsException;
import com.portfolio.raven.exceptions.UsernameAlreadyExistExceotion;
import com.portfolio.raven.mappers.UserMapper;
import com.portfolio.raven.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
            throw new EmailAlreadyExistsException("Email is already registered.");
        }
        if(userRepository.existsByUsername(dto.username())){
            throw new UsernameAlreadyExistExceotion("Username is already in use.");
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
                .orElseThrow(() -> new RuntimeException("User not found."));
        return userMapper.userDetail(user);
    }

    @Transactional
    public UserDetail updateEmail(UUID id, UpdateEmailDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        if(!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())){
            throw new RuntimeException("Esse email já está em uso.");
        }
        user.setEmail(dto.email());
        userRepository.save(user);

        return userMapper.userDetail(user);
    }

    @Transactional
    public UserDetail updateUsername(UUID id, UpdateUsernameDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        if(!user.getUsername().equals(dto.Newusername()) && userRepository.existsByUsername(dto.Newusername())){
            throw new RuntimeException("Esse username já está em uso.");
        }
        user.setUsername(dto.Newusername());
        userRepository.save(user);
        return userMapper.userDetail(user);
    }

    @Transactional
    public void deleteUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        user.setStatus(false);
        userRepository.save(user);
    }




   /* public UserDetail getCurrentUser(){
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

    } */




}
