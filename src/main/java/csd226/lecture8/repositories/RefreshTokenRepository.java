package csd226.lecture8.repositories;

import csd226.lecture8.data.Account;
import csd226.lecture8.data.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    int deleteByAccount(Account account);
}

