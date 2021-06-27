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
    @Query(value =
            "update addresses set user_id = null where user_id = ?1 ; " +
            "update donations set user_id = null where user_id = ?1 ; " +
            "delete from users_roles where user_id = ?1 ; " +
            "delete from users where id = ?1 ;",
            nativeQuery = true)
    void removeUser(Long id);
}