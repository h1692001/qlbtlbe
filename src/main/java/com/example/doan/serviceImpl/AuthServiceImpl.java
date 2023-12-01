package com.example.doan.serviceImpl;

import com.example.doan.dtos.AuthDto;
import com.example.doan.entities.UserEntity;
import com.example.doan.exception.ApiException;
import com.example.doan.repository.UserRepository;
import com.example.doan.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public AuthDto createUser(AuthDto authDto) {
        AuthDto returnValue = new AuthDto();

        if (userRepository.findByEmail(authDto.getEmail()) != null) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Existed user");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(authDto, userEntity);
        userEntity.setPassword(bCryptPasswordEncoder.encode(authDto.getPassword()));
        userEntity.setAvatar("https://res.cloudinary.com/dyvgnrswn/image/upload/v1684721106/mhqiehkoysbdiquyu88a.png");
        UserEntity storedUser = userRepository.save(userEntity);
        BeanUtils.copyProperties(storedUser, returnValue);

        return returnValue;
    }

    @Override
    public AuthDto resetPassword(AuthDto authDto) {
        AuthDto returnValue = new AuthDto();
        UserEntity userEntity = userRepository.findByEmail(authDto.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(authDto.getPassword()));
        UserEntity storedUser = userRepository.save(userEntity);
        BeanUtils.copyProperties(storedUser, returnValue);
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getPassword(),userEntity.getAuthorities());
    }
}
