package pl.coderslab.charity.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.charity.model.Donation;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT SUM(quantity) FROM Donation")
    Optional<Integer> getTotalBags();

    List<Donation> findByUserEmail(String email, Sort sort);

    @Modifying
    @Transactional
    @Query(value = "update donations set user_id = null where user_id = ?1", nativeQuery = true)
    void unbindUser(Long id);
}
