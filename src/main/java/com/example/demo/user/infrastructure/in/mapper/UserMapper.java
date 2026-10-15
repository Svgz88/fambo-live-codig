package com.example.demo.user.infrastructure.in.mapper;

import com.example.demo.user.domain.model.User;
import com.example.demo.user.infrastructure.out.persistence.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity mapToEntity(User user){
        if (user == null) return null;

        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setEmail(user.getEmail());
        userEntity.setRoles(user.getRoles());
        return userEntity;
    }

    public User mapToDomain(UserEntity userEntity){
        if(userEntity == null) return null;

        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getRoles()
        );
    }
}
