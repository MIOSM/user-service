package miosm.user_service.repository;

import miosm.user_service.entity.Subscription;
import miosm.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    
    Optional<Subscription> findByFollowerAndFollowing(User follower, User following);
    
    @Query("SELECT s.following FROM Subscription s WHERE s.follower.id = :userId")
    List<User> findFollowingByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT s.follower FROM Subscription s WHERE s.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.following.id = :userId")
    Long countFollowersByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.follower.id = :userId")
    Long countFollowingByUserId(@Param("userId") UUID userId);
    
    boolean existsByFollowerAndFollowing(User follower, User following);
}
