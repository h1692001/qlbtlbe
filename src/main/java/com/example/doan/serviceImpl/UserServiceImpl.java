package com.example.doan.serviceImpl;

import com.example.doan.dtos.AuthDto;
import com.example.doan.entities.UserEntity;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.UserRepository;
import com.example.doan.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public AuthDto getUser(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);
        if(userEntity==null) throw new ApiException(HttpStatus.BAD_REQUEST,"Not found user");
        AuthDto returnValue=new AuthDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        returnValue.setUserId(userEntity.getUserId());
        return returnValue;
    }

    @Override
    public UserEntity getUserById(Long userId) {
        UserEntity returnValue=userRepository.findById(userId).get();
        return returnValue;
    }

    @Override
    public void updateUserById(Long userId, UserEntity userEntity) {
        UserEntity user=userRepository.findById(userId).get();
        userRepository.save(userEntity);
    }

    @Override
    public AuthDto updateUserByIdWithImage(Long userId, AuthDto authDto) {
        UserEntity user=userRepository.findById(userId).get();
        user.setEmail(authDto.getEmail());
        user.setAvatar(authDto.getAvatar());
        UserEntity storedUser=userRepository.save(user);
        AuthDto returnValue=new AuthDto();
        BeanUtils.copyProperties(storedUser,returnValue);
        returnValue.setUserId(user.getUserId());
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}
