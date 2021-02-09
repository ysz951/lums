package com.zhou.lums.respository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.zhou.lums.model.User;
import com.zhou.lums.model.User.Role;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<User> findAllOrderedByName(String name);

    List<User> findAllByEmail(String email);

    List<User> findAllByRole(Role role);
    @Query("SELECT COUNT(u) FROM User u")
    Long countById();
//    Long countById();
}
