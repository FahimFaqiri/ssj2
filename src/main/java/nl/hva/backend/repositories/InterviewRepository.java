package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findAll();

    Optional<Interview> findById(Long id);

    List<Interview> findInterviewsByBelongsToUser(Long id);

    void deleteById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Override
    <S extends Interview> S save(S entity);
}
