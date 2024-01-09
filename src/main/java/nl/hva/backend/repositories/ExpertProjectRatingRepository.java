package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.ExpertProjectRating;
import nl.hva.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ExpertProjectRatingRepository extends JpaRepository<ExpertProjectRating, Long> {

    List<ExpertProjectRating> findExpertProjectRatingByUser(User user);

    @Modifying(clearAutomatically = true)
    @Transactional
    <S extends User> S save(S entity);

}
