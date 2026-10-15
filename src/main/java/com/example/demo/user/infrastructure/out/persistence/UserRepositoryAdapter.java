package com.example.demo.user.infrastructure.out.persistence;

import com.example.demo.user.domain.port.out.UserRepositoryPort;
import com.example.demo.user.domain.model.User;
import com.example.demo.user.infrastructure.in.mapper.UserMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserEntityRepository jpaUserEntityRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(
            JpaUserEntityRepository jpaUserEntityRepository, UserMapper userMapper) {
        this.jpaUserEntityRepository = jpaUserEntityRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserEntityRepository.findByUsername(username).map(userMapper::mapToDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserEntityRepository.findByEmail(email).map(userMapper::mapToDomain);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.mapToEntity(user);
        UserEntity savedEntity = jpaUserEntityRepository.save(userEntity);
        return userMapper.mapToDomain(savedEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserEntityRepository.existsUserEntityByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> userEntityList = jpaUserEntityRepository.findAll();
        return userEntityList.stream().map(e -> userMapper.mapToDomain(e)).toList();
    }
}