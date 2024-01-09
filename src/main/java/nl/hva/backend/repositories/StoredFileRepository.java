package nl.hva.backend.repositories;

import nl.hva.backend.models.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {

    Optional<StoredFile> findUserFileByName(String name);

    boolean existsByName(String filename);

    void deleteByName(String filename);
}
