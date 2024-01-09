package nl.hva.backend.repositories;

import jakarta.transaction.Transactional;
import nl.hva.backend.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAll();

    Optional<Client> findById(Long id);

    void deleteById(Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Override
    <S extends Client> S save(S entity);
}
