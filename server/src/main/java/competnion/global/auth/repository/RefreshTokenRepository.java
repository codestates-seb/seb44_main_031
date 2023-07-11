package competnion.global.auth.repository;

import competnion.global.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    RefreshToken findTokenByUserId(long userId);

    void deleteTokenByUserId(long userId);
}
