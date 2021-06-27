package pl.coderslab.charity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String username);

    @Modifying
    @Transactional
    @Query(value = "delete from users_roles where user_id = ?1", nativeQuery = true)
    void clearRoles(Long id);
}