package com.example.demo.user.infrastructure.out.persistence;

import com.example.demo.user.domain.model.User;
import com.example.demo.user.domain.port.out.RefreshTokenStorePort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class RefreshTokenAdapter implements RefreshTokenStorePort {

    private final JpaRefreshTokenEntityRepository jpaRefreshTokenEntityRepository;
    private final JpaUserEntityRepository jpaUserEntityRepository;

    public RefreshTokenAdapter(JpaRefreshTokenEntityRepository jpaRefreshTokenEntityRepository, JpaUserEntityRepository jpaUserEntityRepository) {
        this.jpaRefreshTokenEntityRepository = jpaRefreshTokenEntityRepository;
        this.jpaUserEntityRepository = jpaUserEntityRepository;
    }

    @Override
    @Transactional
    public void saveToken(UUID tokenId, User user, Instant expiryDate) {
        UserEntity userEntity = jpaUserEntityRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(tokenId,userEntity, expiryDate);

        jpaRefreshTokenEntityRepository.save(refreshTokenEntity);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByTokenId(UUID tokenId) {
        return jpaRefreshTokenEntityRepository.findById(tokenId)
                .filter(token -> token
                        .getExpiryDate()
                        .isAfter(Instant.now()))
                .map(token -> {
                    UserEntity userEntity = token.getUserEntity();
                    return new User(
                            userEntity.getId(),
                            userEntity.getUsername(),
                            userEntity.getEmail(),
                            userEntity.getRoles());
                });

    }

    @Override
    @Transactional
    public void deleteTokenById(UUID tokenId) {
        jpaRefreshTokenEntityRepository.deleteById(tokenId);
    }

    @Override
    @Transactional
    public void deleteAllTokenForUser(User user) {
        jpaRefreshTokenEntityRepository.deleteByUserId(user.getId());

    }

}
