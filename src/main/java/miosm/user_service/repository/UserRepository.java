package miosm.user_service.repository;

import miosm.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.bio) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<User> searchUsers(@Param("query") String query);
    
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.bio) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY " +
           "CASE WHEN LOWER(u.username) = LOWER(:query) THEN 1 " +
           "     WHEN LOWER(u.username) LIKE LOWER(CONCAT(:query, '%')) THEN 2 " +
           "     WHEN LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) THEN 3 " +
           "     ELSE 4 END")
    List<User> searchUsersRanked(@Param("query") String query);
}
